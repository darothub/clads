package com.decagon.clads.controllers;

import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.LoginService;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
public class LoginController {
    private final SuccessResponseHandler successResponseHandler;
    private final LoginService loginService;
    @PostMapping(path = "/login")
    public ResponseEntity<ResponseModel> login(@Valid @RequestBody LoginRequest loginRequest, @RequestHeader(value = "Authorization", required = false) String auth) {
        String token = loginService.loginService(loginRequest);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, token);
    }
}
