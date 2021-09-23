package com.decagon.clads.repositories.chat;

import com.decagon.clads.entities.chat.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(readOnly = true)
public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Transactional
    @Modifying
    @Query("SELECT c FROM Conversation c WHERE c.user1Id = ?1 OR c.user2Id = ?1")
    Collection<Conversation> getConversationAndMessages(long id);

    @Transactional
    @Query("SELECT m.conversation FROM ChatMessage m WHERE " +
            "(m.receiverId = ?1 OR m.senderId = ?1) AND (m.senderId = ?2 OR m.receiverId = ?2) ")
    Conversation findConversationById(long receiverId, long senderId);

}
