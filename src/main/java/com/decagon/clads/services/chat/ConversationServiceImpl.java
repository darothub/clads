package com.decagon.clads.services.chat;

import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.repositories.chat.ConversationRepository;
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
        return conversationRepository.getConversationAndMessages(JwtFilter.userId);
    }
}
