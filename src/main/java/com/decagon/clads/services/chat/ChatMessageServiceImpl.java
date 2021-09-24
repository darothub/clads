package com.decagon.clads.services.chat;

import com.decagon.clads.config.AblyConfig;
import com.decagon.clads.entities.chat.ChatMessage;
import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.repositories.chat.ChatMessageRepository;
import com.decagon.clads.repositories.chat.ConversationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ably.lib.realtime.Channel;
import org.springframework.beans.factory.annotation.Value;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.types.AblyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService{

    @Value( "${ably.channel}" )
    private String channelName;
    private final ObjectMapper mapper;
    private final Channel channel;
    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;

    @Override
    public ChatMessage addChatMessage(ChatMessage chatMessage) throws AblyException, JsonProcessingException {
        String topic = chatMessage.getReceiverId() + chatMessage.getSenderId() + channelName;
        if (chatMessage.getConversation() == null){
           Conversation c = conversationRepository.findConversationById(chatMessage.getReceiverId(), chatMessage.getSenderId());
           if(c != null){
               c.addMessage(chatMessage);
               Conversation updated = conversationRepository.save(c);
               ChatMessage newChatMessage = updated.getMessages().get(updated.getMessages().size()-1);
               newChatMessage.setChatName(topic);
               channel.publish(newChatMessage.getChatName(), mapper.writeValueAsString(newChatMessage));
               return newChatMessage;
            }
           Conversation newConvo = new Conversation();
           newConvo.setUser1Id(chatMessage.getSenderId());
           newConvo.setUser2Id(chatMessage.getReceiverId());
           newConvo.addMessage(chatMessage);
           conversationRepository.save(newConvo);
           chatMessage.setChatName(topic);
           channel.publish(chatMessage.getChatName(),mapper.writeValueAsString(chatMessage));
           return chatMessage;
        }
        Optional<Conversation> oldConvo = conversationRepository.findById(chatMessage.getConversation().getId());
        oldConvo.ifPresent(conversation -> conversation.getMessages().add(chatMessage));
        conversationRepository.save(oldConvo.get());
        channel.publish(chatMessage.getChatName(), mapper.writeValueAsString(chatMessage));
        return chatMessage;
    }
}
