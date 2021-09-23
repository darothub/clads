package com.decagon.clads.controllers.chat;

import com.decagon.clads.controllers.SuccessResponseHandler;
import com.decagon.clads.entities.chat.ChatMessage;
import com.decagon.clads.entities.chat.Conversation;
import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.chat.ConversationServiceImpl;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
@Slf4j
public class ConversationController {
    private final SuccessResponseHandler successResponseHandler;
    private final ConversationServiceImpl conversationService;
    @PostMapping(path = "/conversations")
    public ResponseEntity<ResponseModel> postMessage(@Valid @RequestBody Conversation conversation) {
        Conversation c = conversationService.addConversation(conversation);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, conversation);
    }
    @GetMapping(path = "/conversations")
    public ResponseEntity<ResponseModel> getUserConversations() {
        Collection <Conversation> c = conversationService.getUserConversations();
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, c);
    }
}
