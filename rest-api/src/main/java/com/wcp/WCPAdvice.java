package com.wcp;

import com.wcp.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WCPAdvice {

    private final Logger log = LoggerFactory.getLogger(WCPAdvice.class);

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<String> custom(NumberFormatException e) {
        log.error("key should not be empty or String. Please Check key", e);

        final ErrorResponse response
                = ErrorResponse
                .create()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("key should not be empty or String. Please Check key");

        return new ResponseEntity<String>(response.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
