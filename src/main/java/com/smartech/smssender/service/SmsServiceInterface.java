package com.smartech.smssender.service;

import com.smartech.smssender.model.sms.SmsResponseModel;

public interface SmsServiceInterface {
    public SmsResponseModel send(String number, String body) throws Exception;
}
