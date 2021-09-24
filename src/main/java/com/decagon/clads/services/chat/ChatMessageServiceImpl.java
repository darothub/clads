package com.decagon.clads.services.chat;

import com.decagon.clads.config.AblyConfig;
import com.decagon.clads.entities.chat.ChatMessage;
import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.repositories.chat.ChatMessageRepository;
import com.decagon.clads.repositories.chat.ConversationRepository;
import com.google.api.client.util.Value;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.types.AblyException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService{

    @Value( "${ably.channel}" )
    private String channelName;
    private AblyConfig ablyConfig;
    private AblyRealtime ablyRealtime;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;

    private AblyRealtime getRealtime() {
        if (ablyRealtime == null) {
            ablyRealtime = ablyConfig.ablyRealtime();
        }
        return ablyRealtime;
    }
    private boolean publishToChannel(String channelName, String name, Object data) {
        try {
            getRealtime().channels.get(channelName).publish(name, data);
        } catch (AblyException err) {
            System.out.println(err.getMessage());
            return false;
        }
        return true;
    }
    @Override
    public ChatMessage addChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getConversation() == null){
           Conversation c = conversationRepository.findConversationById(chatMessage.getReceiverId(), chatMessage.getSenderId());
           if(c != null){
               c.addMessage(chatMessage);
               Conversation updated = conversationRepository.save(c);
               ChatMessage newChatMessage = updated.getMessages().get(updated.getMessages().size()-1);
               newChatMessage.setChatName(updated.getId().toString());
               publishToChannel(channelName, newChatMessage.getChatName(), newChatMessage);
               return newChatMessage;
            }
           Conversation newConvo = new Conversation();
           newConvo.setUser1Id(chatMessage.getSenderId());
           newConvo.setUser2Id(chatMessage.getReceiverId());
           newConvo.addMessage(chatMessage);
           Conversation savedConvo = conversationRepository.save(newConvo);
           chatMessage.setChatName(savedConvo.getId().toString());
           publishToChannel(channelName, chatMessage.getChatName(), chatMessage);
           return chatMessage;
        }
        Optional<Conversation> oldConvo = conversationRepository.findById(chatMessage.getConversation().getId());
        oldConvo.ifPresent(conversation -> conversation.getMessages().add(chatMessage));
        conversationRepository.save(oldConvo.get());
        publishToChannel(channelName, chatMessage.getChatName(), chatMessage);
        return chatMessage;
    }
}
