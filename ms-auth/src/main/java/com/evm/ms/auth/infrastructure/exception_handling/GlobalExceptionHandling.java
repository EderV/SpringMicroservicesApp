package com.evm.ms.auth.infrastructure.exception_handling;

import com.evm.ms.auth.domain.exceptions.UserAlreadyRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Exception caught by @ControllerAdvice. Message: ".concat(ex.getMessage()));
        log.error("STACK TRACE: ".concat(Arrays.toString(ex.getStackTrace())));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleUserAlreadyRegisteredException(UserAlreadyRegisteredException ex) {
        log.error("Exception caught by @ControllerAdvice. Message: ".concat(ex.getMessage()));
        log.error("STACK TRACE: ".concat(Arrays.toString(ex.getStackTrace())));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

}
