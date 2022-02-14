package com.decagon.clads.artisans.controllers;

import com.decagon.clads.artisans.entities.Artisan;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.artisans.services.auth.RegistrationService;
import com.decagon.clads.utils.ConstantUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class RegistrationController {
    private final RegistrationService registrationService;
    private final SuccessResponseHandler successResponseHandler;
    private final ConstantUtils constantUtils;
    private final ObjectMapper objectMapper;

    @GetMapping("/home")
    public ResponseEntity<Object> home() {
        log.info(constantUtils.host);
        return ResponseEntity.ok("Welcome to Clads home");
    }
    @PostMapping("/artisans/register")
    public ResponseEntity<ResponseModel> register(@Valid @RequestBody Artisan artisan) {
        String token = (String) registrationService.register(artisan).join();
        ArtisanDTO artisanDTO = objectMapper.convertValue(artisan, ArtisanDTO.class);
        return handleSuccessResponseEntity("User added successfully", HttpStatus.CREATED, artisanDTO);
    }
    @GetMapping(path = "/confirm")
    public ResponseEntity<ResponseModel> confirm(@RequestParam("token") String token) {
        registrationService.confirmToken(token);
        return handleSuccessResponseEntity("Email successfully confirmed", HttpStatus.OK);
    }

    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        return successResponseHandler.handleSuccessResponseEntity(message, status, Optional.of(payload));
    }
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status) {
        return successResponseHandler.handleSuccessResponseEntity(message, status);
    }
}


