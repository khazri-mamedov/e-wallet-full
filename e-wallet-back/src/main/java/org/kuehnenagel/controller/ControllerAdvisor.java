package org.kuehnenagel.controller;

import org.kuehnenagel.util.ApiError;
import org.kuehnenagel.util.BalanceUnderflowException;
import org.kuehnenagel.util.NoSuchEntityException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handling common errors from business logic
 */
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(BalanceUnderflowException.class)
    public ResponseEntity<?> handleBalanceException(final BalanceUnderflowException blEx, WebRequest request) {
        final String message = String.format("Balance underflow, current is %s!", blEx.getCurrentBalance().toString());
        ApiError error = new ApiError(blEx.getLocalizedMessage(), message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleTopConstraints(ConstraintViolationException ex) {
        ApiError error = new ApiError(ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles not found entity from any repository
     */
    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<?> handleNoEntity(final NoSuchEntityException ex, WebRequest request) {
        final String message = String.format("Resource with id %s not found!", ex.getEntityId());
        ApiError error = new ApiError(ex.getLocalizedMessage(), message);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    /**
     * For handling bean validation
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
        
        ex.getBindingResult().getGlobalErrors()
                .forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));
    
        ApiError apiError =
                new ApiError(ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
                ex, apiError, headers, HttpStatus.BAD_REQUEST, request);
    }
}
