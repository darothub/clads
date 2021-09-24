package com.decagon.clads.services.chat;

import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.filter.JwtFilter;
import com.decagon.clads.jwt.JWTUtility;
import com.decagon.clads.repositories.chat.ChatMessageRepository;
import com.decagon.clads.repositories.chat.ConversationRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = {"conversation"})
public class ConversationServiceImpl implements ConversationService{
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public Collection<Conversation> getUserConversations() {
        return conversationRepository.getConversationAndMessages(JwtFilter.userId);
    }
}
