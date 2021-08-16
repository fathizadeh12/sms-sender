package com.smartech.smssender.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalSmsApiCallerService {
    @Value("${first.sms.service.base.url}")
    private String FIRST_SMS_SERVICE_BASE_URL;

    @Value("${second.sms.service.base.url}")
    private String SECOND_SMS_SERVICE_BASE_URL;

    final RestTemplate restTemplate;

    public ExternalSmsApiCallerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity callFirstSmsService(String number, String body) {
        return restTemplate.exchange(FIRST_SMS_SERVICE_BASE_URL + "sms/send?number=" + number + "&body=" + body, HttpMethod.GET, null, ResponseEntity.class);

    }

    public ResponseEntity callSecondSmsService(String number, String body) {

        return restTemplate.exchange(SECOND_SMS_SERVICE_BASE_URL + "sms/send?number=" + number + "&body=" + body, HttpMethod.GET, null, ResponseEntity.class);
    }
}
