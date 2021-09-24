package com.decagon.clads.services.chat;

import com.decagon.clads.entities.chat.ChatMessage;
import com.decagon.clads.entities.chat.Conversation;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.ably.lib.types.AblyException;

public interface ChatMessageService {
   ChatMessage addChatMessage(ChatMessage chatMessage) throws AblyException, JsonProcessingException;
}
