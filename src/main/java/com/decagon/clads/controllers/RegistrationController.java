package com.decagon.clads.controllers;

import com.decagon.clads.entities.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.model.response.SuccessResponse;
import com.decagon.clads.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

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
    @GetMapping(path = "/confirm")
    public ResponseEntity<ResponseModel> confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return handleSuccessResponseEntity("Email successfully confirmed", HttpStatus.OK, LocalDateTime.now());
    }
    @GetMapping(path = "/profile")
    public ResponseEntity<ResponseModel> Hello() {
        return handleSuccessResponseEntity("Email successfully confirmed", HttpStatus.OK, LocalDateTime.now());
    }
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        successResponse.setPayload(payload);
        return new ResponseEntity<>(successResponse, status);
    }
}


