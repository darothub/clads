package com.decagon.clads.services.chat;

import com.decagon.clads.entities.chat.Conversation;

import java.util.Collection;

interface ConversationService {
    Conversation addConversation(Conversation conversation);
    Collection<Conversation> getUserConversations();
}
