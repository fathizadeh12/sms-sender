package com.smartech.smssender.exception;

public class SmsNotSentException extends RuntimeException {
    public SmsNotSentException(String message) {
        super(message);
    }
}
