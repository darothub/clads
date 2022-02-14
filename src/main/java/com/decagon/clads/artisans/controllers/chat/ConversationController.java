package com.decagon.clads.artisans.controllers.chat;

import com.decagon.clads.artisans.controllers.SuccessResponseHandler;
import com.decagon.clads.chat.entities.Conversation;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.chat.services.ConversationServiceImpl;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
@Slf4j
public class ConversationController {
    private final SuccessResponseHandler successResponseHandler;
    private final ConversationServiceImpl conversationService;

    @GetMapping(path = "/conversations")
    public ResponseEntity<ResponseModel> getUserConversations() {
        Collection <Conversation> c = conversationService.getUserConversations();
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, Optional.of(c));
    }
}
