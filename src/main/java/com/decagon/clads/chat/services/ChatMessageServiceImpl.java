package com.decagon.clads.chat.services;

import com.decagon.clads.chat.entities.ChatMessage;
import com.decagon.clads.chat.entities.Conversation;
import com.decagon.clads.chat.repositories.ConversationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ably.lib.realtime.Channel;
import io.ably.lib.types.Message;
import org.springframework.beans.factory.annotation.Value;
import io.ably.lib.types.AblyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService{

    @Value( "${ably.channel}" )
    private String channelName;
    private final ObjectMapper mapper;
    private final Channel channel;
    private final ConversationRepository conversationRepository;

    @Override
    public ChatMessage addChatMessage(ChatMessage chatMessage) throws AblyException, JsonProcessingException {
//        chatMessage.setSenderId("darot@gmail.com");
        chatMessage.setChatName(chatMessage.getReceiverId());
        if (chatMessage.getConversation() == null){
           Conversation c = conversationRepository.findConversationById(chatMessage.getReceiverId(), chatMessage.getSenderId());

           if(c != null){
               c.addMessage(chatMessage);
               Conversation updated = conversationRepository.save(c);
               ChatMessage newChatMessage = updated.getMessages().get(updated.getMessages().size()-1);

               channel.publish("onNewMessage", mapper.writeValueAsString(newChatMessage));
               return newChatMessage;
            }
           Conversation newConvo = new Conversation();
           newConvo.setUser1Id(chatMessage.getSenderId());
           newConvo.setUser2Id(chatMessage.getReceiverId());
           newConvo.addMessage(chatMessage);
           conversationRepository.save(newConvo);
           channel.publish("onNewMessage",mapper.writeValueAsString(chatMessage));
           return chatMessage;
        }
        Optional<Conversation> oldConvo = conversationRepository.findById(chatMessage.getConversation().getId());
        oldConvo.ifPresent(conversation -> conversation.getMessages().add(chatMessage));
        conversationRepository.save(oldConvo.get());
        channel.publish("onNewMessage", mapper.writeValueAsString(chatMessage));
        return chatMessage;
    }

    void subscription(String channelName, String message) throws AblyException {
        channel.subscribe(channelName, new Channel.MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    channel.publish(channelName, mapper.writeValueAsString(message));
                } catch (AblyException e) {
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
