package com.decagon.clads.controllers;

import com.decagon.clads.entities.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.model.response.SuccessResponse;
import com.decagon.clads.services.RegistrationService;
import com.decagon.clads.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final SuccessResponse successResponse;

    @PostMapping("/artisans")
    public ResponseEntity<ResponseModel> register(@Valid @RequestBody Artisan artisan) {
        ArtisanDTO artisanAdded = registrationService.register(artisan);
        return handleSuccessResponseEntity("User added successfully", HttpStatus.CREATED, artisanAdded);
    }
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        successResponse.setPayload(payload);
        return new ResponseEntity<>(successResponse, status);
    }
}
