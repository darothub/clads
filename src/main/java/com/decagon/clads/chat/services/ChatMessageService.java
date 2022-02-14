package com.decagon.clads.chat.services;

import com.decagon.clads.chat.entities.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.ably.lib.types.AblyException;

public interface ChatMessageService {
   ChatMessage addChatMessage(ChatMessage chatMessage) throws AblyException, JsonProcessingException;
}
