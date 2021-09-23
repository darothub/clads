package com.decagon.clads.services.chat;

import com.decagon.clads.entities.chat.ChatMessage;
import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.repositories.chat.ChatMessageRepository;
import com.decagon.clads.repositories.chat.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatMessageServiceImpl implements ChatMessageService{

    private final ChatMessageRepository chatMessageRepository;
    private final ConversationRepository conversationRepository;


    @Override
    public ChatMessage addChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getConversation() == null){
           Conversation c = conversationRepository.findConversationById(chatMessage.getReceiverId(), chatMessage.getSenderId());
           if(c != null){
               c.addMessage(chatMessage);
               Conversation updated = conversationRepository.save(c);
               return updated.getMessages().get(updated.getMessages().size()-1);
            }
           Conversation newConvo = new Conversation();
           newConvo.setUser1Id(chatMessage.getSenderId());
           newConvo.setUser2Id(chatMessage.getReceiverId());
           newConvo.addMessage(chatMessage);
           conversationRepository.save(newConvo);
           return chatMessage;
        }
        Optional<Conversation> oldConvo = conversationRepository.findById(chatMessage.getId());
        oldConvo.ifPresent(conversation -> conversation.getMessages().add(chatMessage));
        conversationRepository.save(oldConvo.get());
        return chatMessage;
    }
}
