package com.decagon.clads.chat.repositories;

import com.decagon.clads.chat.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Transactional
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.receiverId = ?1 OR m.senderId = ?1) ")
    Collection<ChatMessage> findAllChatMessages(long userId);
}
