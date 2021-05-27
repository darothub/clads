package com.decagon.clads.controllers;

import com.decagon.clads.model.response.ResponseModel;
import com.decagon.clads.model.response.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
@AllArgsConstructor
@Component
public class SuccessResponseHandleImpl implements SuccessResponseHandler {

    private final SuccessResponse successResponse;
    @Override
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Object payload) {
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        successResponse.setPayload(payload);
        return new ResponseEntity<>(successResponse, status);
    }
}
