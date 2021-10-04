package com.decagon.clads.controllers;

import com.decagon.clads.model.response.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface SuccessResponseHandler {
    ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Optional<Object> payload);
    ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status);
}
