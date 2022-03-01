package com.decagon.clads.artisans.controllers;

import com.decagon.clads.model.request.LoginRequest;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.artisans.services.auth.LoginService;
import com.decagon.clads.utils.ConstantUtils;
import com.decagon.clads.utils.Role;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
@Slf4j
public class LoginController {
    private final SuccessResponseHandler successResponseHandler;
    private final LoginService loginService;
    @PostMapping(path = "/artisan/login")
    public ResponseEntity<ResponseModel> loginArtisan(@Valid @RequestBody LoginRequest loginRequest) {
        String token = loginService.loginArtisanService(loginRequest);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, Optional.of(token));
    }
    @PostMapping(path = "/customer/login")
    public ResponseEntity<ResponseModel> loginCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        String token = loginService.loginCustomerService(loginRequest);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, Optional.of(token));
    }

    @PostMapping(path = "/login/google")
    public ResponseEntity<ResponseModel> loginWithGoogle(@RequestHeader(value = "Authorization") String auth, @RequestHeader(required = false, value = "Role") Role role) {
        if (auth == null){
            log.info("Header {} {}", auth,role);
        }
        String token = loginService.loginWithGoogleService(auth, role);
        return successResponseHandler.handleSuccessResponseEntity("User logged in successfully", HttpStatus.OK, Optional.of(token));
    }

}
