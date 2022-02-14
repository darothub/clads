package com.decagon.clads.chat.services;

import com.decagon.clads.chat.entities.Conversation;

import java.util.Collection;

interface ConversationService {
    Collection<Conversation> getUserConversations();
}
