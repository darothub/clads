package com.decagon.clads.controllers.chat;

import com.decagon.clads.config.AblyConfig;
import com.decagon.clads.controllers.SuccessResponseHandler;
import com.decagon.clads.entities.chat.ChatMessage;
import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.chat.ChatMessageServiceImpl;
import com.decagon.clads.services.chat.ConversationServiceImpl;
import com.decagon.clads.utils.ConstantUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.ably.lib.realtime.AblyRealtime;
import io.ably.lib.realtime.Channel;
import io.ably.lib.rest.AblyRest;
import io.ably.lib.types.AblyException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
@Slf4j
public class ChatMessageController {

    private final SuccessResponseHandler successResponseHandler;
    private final ChatMessageServiceImpl chatMessageService;
    private final Channel channel;


    @PostMapping(path = "/message")
    public ResponseEntity<ResponseModel> postMessage(@Valid @RequestBody ChatMessage chatMessage) throws AblyException, JsonProcessingException {
        ChatMessage c = chatMessageService.addChatMessage(chatMessage);
        return successResponseHandler.handleSuccessResponseEntity("Message added successfully", HttpStatus.OK, Optional.of(c));
    }
}
