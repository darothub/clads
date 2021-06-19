package com.decagon.clads.controllers;

import com.decagon.clads.model.response.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface SuccessResponseHandler {
    ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload);
    ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status);
}
