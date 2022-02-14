package com.decagon.clads.chat.services;

import com.decagon.clads.chat.entities.Conversation;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.chat.repositories.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = {"conversation"})
public class ConversationServiceImpl implements ConversationService{
    private final ConversationRepository conversationRepository;

    @Override
    public Collection<Conversation> getUserConversations() {
        return conversationRepository.getConversationAndMessages(JwtFilter.userName);
    }
}
