package com.decagon.clads.controllers;

import com.decagon.clads.entities.artisan.AuthRole;
import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.services.LoginService;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
@Slf4j
public class LoginController {
    private final SuccessResponseHandler successResponseHandler;
    private final LoginService loginService;
    @PostMapping(path = "/login")
    public ResponseEntity<ResponseModel> login(@Valid @RequestBody LoginRequest loginRequest) {

        String token = loginService.loginService(loginRequest);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, token);
    }

    @PostMapping(path = "/login/google")
    public ResponseEntity<ResponseModel> loginWithGoogle(@RequestBody AuthRole role, @RequestHeader(value = "Authorization") String auth) {
//        log.info("Herererer {}", auth);
        String token = loginService.loginWithGoogleService(role, auth);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, token);
    }

}
