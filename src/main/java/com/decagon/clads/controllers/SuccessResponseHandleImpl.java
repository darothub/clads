package com.decagon.clads.controllers;

import com.decagon.clads.model.response.ResponseDTO;
import com.decagon.clads.model.response.ResponseDTOWithoutPayload;
import com.decagon.clads.model.response.ResponseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@AllArgsConstructor
@Component
public class SuccessResponseHandleImpl implements SuccessResponseHandler {

    private final ResponseDTO successResponse;
    private final ResponseDTOWithoutPayload responseDTOWithoutPayload;
    private final ModelMapper modelMapper;
    @Override
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status, Optional<Object> payload) {
        successResponse.setMessage(message);
        successResponse.setStatus(status.value());
        payload.ifPresent(successResponse::setPayload);
        return new ResponseEntity<>(successResponse, status);
    }

    @Override
    public ResponseEntity<ResponseModel> handleSuccessResponseEntity(String message, HttpStatus status) {
        responseDTOWithoutPayload.setMessage(message);
        responseDTOWithoutPayload.setStatus(status.value());
        return new ResponseEntity<>(responseDTOWithoutPayload, status);
    }
}
