package com.decagon.clads.artisans.controllers;

import com.decagon.clads.customer.entities.Client;
import com.decagon.clads.customer.entities.ClientDTO;
import com.decagon.clads.model.dto.ArtisanDTO;
import com.decagon.clads.model.dto.CladUser;
import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.artisans.services.auth.RegistrationServiceImpl;
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
    private final RegistrationServiceImpl registrationServiceImpl;
    private final SuccessResponseHandler successResponseHandler;
    private final ConstantUtils constantUtils;
    private final ObjectMapper objectMapper;

    @GetMapping("/home")
    public ResponseEntity<Object> home() {
        log.info(constantUtils.host);
        return ResponseEntity.ok("Welcome to Clads home");
    }
    @PostMapping("/artisan/register")
    public ResponseEntity<ResponseModel> register(@Valid @RequestBody CladUser cladUser) {
        ArtisanDTO artisanDTO = registrationServiceImpl.registerArtisan(cladUser);
        return handleSuccessResponseEntity("User created successfully", HttpStatus.CREATED, artisanDTO);
    }
    @PostMapping("/customer/register")
    public ResponseEntity<ResponseModel> registerCustomer(@Valid @RequestBody CladUser cladUser) {
        ClientDTO clientDTO = registrationServiceImpl.registerCustomer(cladUser);
        return handleSuccessResponseEntity("User created successfully", HttpStatus.CREATED, clientDTO);
    }
    @GetMapping(path = "/confirm")
    public ResponseEntity<ResponseModel> confirm(@RequestParam("token") String token) {
        boolean confirmed = registrationServiceImpl.confirmToken(token);
        if (confirmed){
            return handleSuccessResponseEntity("Email successfully confirmed", HttpStatus.OK);
        }
        return handleSuccessResponseEntity("Email not confirmed", HttpStatus.BAD_GATEWAY);
    }

    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        return successResponseHandler.handleSuccessResponseEntity(message, status, Optional.of(payload));
    }
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status) {
        return successResponseHandler.handleSuccessResponseEntity(message, status);
    }
}


