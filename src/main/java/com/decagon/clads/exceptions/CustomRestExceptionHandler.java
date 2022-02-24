//package com.decagon.clads.exceptions;
//
//
//import com.decagon.clads.model.response.ErrorResponse;
//import com.decagon.clads.model.response.ResponseModel;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.extern.slf4j.Slf4j;
//import org.hibernate.exception.SQLGrammarException;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.http.converter.HttpMessageNotWritableException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.transaction.TransactionSystemException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import javax.persistence.EntityNotFoundException;
//import javax.validation.ConstraintViolation;
//import javax.validation.ConstraintViolationException;
//import java.net.SocketTimeoutException;
//import java.net.UnknownHostException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.NoSuchElementException;
//
//import static org.springframework.http.HttpStatus.*;
//
//@Data
//@AllArgsConstructor
//@ControllerAdvice
//@Slf4j
//@EqualsAndHashCode(callSuper = false)
//public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
//
//    private ResponseModel responseModel;
//    private ErrorResponse error;
//
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        return buildResponseEntity(getValidationErrors(ex.getBindingResult().getFieldErrors()));
//
//    }
//
//    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
//    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
//            MethodArgumentTypeMismatchException ex) {
//        return errorHandlerController(ex, BAD_REQUEST);
//    }
//
//    @ExceptionHandler({SQLGrammarException.class})
//    public ResponseEntity<Object> handleSQLGrammarException(
//            SQLGrammarException ex) {
//        return errorHandlerController(ex, INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler({UsernameNotFoundException.class})
//    public ResponseEntity<Object> handleUsernameNotFoundException(
//            UsernameNotFoundException ex) {
//        return errorHandlerController(ex, NOT_FOUND);
//    }
//
//    @ExceptionHandler({SocketTimeoutException.class})
//    public ResponseEntity<Object> handleSocketTimeoutException(
//            SocketTimeoutException ex) {
//        return errorHandlerController(ex, INTERNAL_SERVER_ERROR);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return errorHandlerController(ex, status);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        error.setMessage("Request body is not readable");
//        error.setStatus(BAD_REQUEST.value());
//        return buildResponseEntity(error);
//    }
//
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        error.setMessage(ex.getMessage());
//        error.setStatus(BAD_REQUEST.value());
//        return buildResponseEntity(error);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    protected ResponseEntity<Object> handleEntityNotFound(
//            EntityNotFoundException ex) {
//
//        error.setMessage(NOT_FOUND.toString());
//        error.setStatus(NOT_FOUND.value());
//        error.setPayload(ex.getLocalizedMessage().replace("com.darothub.clientservice.entity.", ""));
//        return buildResponseEntity(error);
//    }
//
//    @ExceptionHandler(EmptyResultDataAccessException.class)
//    protected ResponseEntity<Object> handleEmptyResultDataAccessException(
//            EmptyResultDataAccessException ex) {
//
//        error.setMessage(NOT_FOUND.toString());
//        error.setStatus(NOT_FOUND.value());
//        error.setPayload(ex.getLocalizedMessage().replace("com.darothub.clientservice.entity.", ""));
//        return buildResponseEntity(error);
//    }
//
//    @ExceptionHandler(IllegalStateException.class)
//    protected ResponseEntity<Object> handleIllegalStateException(
//            IllegalStateException ex) {
//        error.setMessage(ex.getMessage());
//        error.setStatus(BAD_REQUEST.value());
//        return buildResponseEntity(error);
//    }
//
//
//    @ExceptionHandler(CustomException.class)
//    protected ResponseEntity<Object> handleCustomException(
//            CustomException ce) {
//        return buildResponseEntity(ce.getError());
//    }
//
//
//    @ExceptionHandler({NoSuchElementException.class})
//    public ResponseEntity<Object> handleNoSuchElement(Exception ex) {
//
//        return errorHandlerController(ex, NOT_FOUND);
//    }
//
//    @ExceptionHandler({UnknownHostException.class})
//    public ResponseEntity<Object> handleUnknownHostException(UnknownHostException ex) {
//        return errorHandlerController(ex, INTERNAL_SERVER_ERROR);
//    }
//
//
//    @ExceptionHandler(TransactionSystemException.class)
//    public ResponseEntity<Object> handleTransactionSystemException(TransactionSystemException ex) {
//        getConstraintValidationErrors(ex);
//        return buildResponseEntity(error);
//    }
//
//    private ResponseEntity<Object> buildResponseEntity(ErrorResponse error) {
//        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
//    }
//
//    private ErrorResponse getValidationErrors(List<FieldError> fieldErrors) {
//
//        error.setStatus(BAD_REQUEST.value());
//        error.setMessage(BAD_REQUEST.toString());
//        Map<String, String> errors = new HashMap<>();
//        fieldErrors.forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
//        error.setPayload(errors);
//        return error;
//    }
//
//    private ErrorResponse getConstraintValidationErrors(TransactionSystemException ex) {
//        getValidationErrors(ex, error);
//        return error;
//    }
//
//    private void getValidationErrors(TransactionSystemException ex, ErrorResponse error) {
//        error.setStatus(BAD_REQUEST.value());
//        error.setMessage(BAD_REQUEST.toString());
//        ConstraintViolationException cve = (ConstraintViolationException) ex.getRootCause();
//        Map<String, String> errors = new HashMap<>();
//        assert cve != null;
//        for (ConstraintViolation<?> violation : cve.getConstraintViolations()) {
//            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
//        }
//        error.setPayload(errors);
//    }
//
//
//    private ResponseEntity<Object> errorHandlerController(Exception ex, HttpStatus status) {
//        error.setMessage(status.toString());
//        error.setStatus(status.value());
//        error.setPayload(ex.getLocalizedMessage());
//        return new ResponseEntity<>(error, HttpStatus.valueOf(error.getStatus()));
//    }
//}
