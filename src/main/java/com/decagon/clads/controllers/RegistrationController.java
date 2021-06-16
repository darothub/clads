package com.decagon.clads.controllers;

import com.decagon.clads.entities.artisan.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.model.response.SuccessResponse;
import com.decagon.clads.services.RegistrationService;
import com.decagon.clads.utils.ConstantUtils;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
@RequestMapping(path = ConstantUtils.BASE_URL)
public class RegistrationController {
    private final RegistrationService registrationService;
    private final SuccessResponseHandler successResponseHandler;

    @PostMapping("/artisans/register")
    public ResponseEntity<ResponseModel> register(@Valid @RequestBody Artisan artisan) {
        String token = (String) registrationService.register(artisan).join();
        return handleSuccessResponseEntity("User added successfully", HttpStatus.CREATED, token);
    }
    @GetMapping(path = "/confirm")
    public ResponseEntity<ResponseModel> confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return handleSuccessResponseEntity("Email successfully confirmed", HttpStatus.OK, LocalDateTime.now());
    }


    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        return successResponseHandler.handleSuccessResponseEntity(message, status, payload);
    }
}


