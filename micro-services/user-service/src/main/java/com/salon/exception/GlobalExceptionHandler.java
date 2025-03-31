package com.salon.exception;

import com.salon.payload.response.ExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionHandler(
            Exception exception,
            WebRequest request
    ) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                exception.getMessage(),
                request.getDescription(false),
                LocalDateTime.now()
        );

        return ResponseEntity.ok(exceptionResponse);
    }
}
