package com.smartech.smssender.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> processBaseException(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        ex.printStackTrace();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SmsNotSentException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> problemInSendinSms(SmsNotSentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        ex.printStackTrace();
        body.put("message", "There is a problem in sending message!");
        body.put("exception", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


}
