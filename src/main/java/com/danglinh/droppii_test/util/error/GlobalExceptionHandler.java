package com.danglinh.droppii_test.util.error;

import com.danglinh.droppii_test.domain.DTO.response.RestResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {
            Exception.class
    })
    public ResponseEntity<RestResponse<Object>> handleEx(Exception e) {
        RestResponse<Object> res = new RestResponse<>();
        res.setError("Exception occured");
        res.setMessage(e.getMessage());
        res.setStatusCode(400);
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(res);
    }


    @ExceptionHandler(DroppiiException.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(DroppiiException e) {
        RestResponse<Object> res = new RestResponse<>();
        res.setError("Exception occured");
        res.setMessage(e.getMessage());
        res.setStatusCode(400);
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(res);
    }

}

