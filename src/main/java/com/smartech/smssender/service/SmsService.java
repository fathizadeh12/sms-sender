package com.smartech.smssender.service;

import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.exception.SmsNotSentException;
import com.smartech.smssender.model.sms.SmsResponseModel;
import com.smartech.smssender.rest.SmsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SmsService implements SmsServiceInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsController.class);

    final RestTemplate restTemplate;

    final ExternalSmsApiCallerService externalSmsApiCallerService;

    final SmsLoggerInterface smsLoggerInterface;


    public SmsService(SmsLoggerInterface smsLoggerInterface, RestTemplate restTemplate, ExternalSmsApiCallerService externalSmsApiCallerService) {
        this.smsLoggerInterface = smsLoggerInterface;
        this.restTemplate = restTemplate;
        this.externalSmsApiCallerService = externalSmsApiCallerService;
    }


    @Override
    public SmsResponseModel send(String number, String body) throws Exception {
        LOGGER.info("sen(), input: number = {}, body= {}", number, body);

        SmsResponseModel smsResponseModel = new SmsResponseModel();

        ResponseEntity<?> responseEntity;

        try {
            responseEntity = externalSmsApiCallerService.callFirstSmsService(number, body);

            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                smsLoggerInterface.logSms(number, body, SmsStatus.SENT);

            } else {
                tryToSendBySecondApi(number, body);
            }
        } catch (Exception e) {
            tryToSendBySecondApi(number, body);
        }

        smsResponseModel.setMessage("Sms hase been sent");
        LOGGER.info("sen(), output: {}", smsResponseModel);
        return smsResponseModel;
    }

    private void tryToSendBySecondApi(String number, String body) {
        LOGGER.info("tryToSendBySecondApi(), input= number{}, body= {}", number, body);

        ResponseEntity<?> responseEntity;
        try {
            responseEntity = externalSmsApiCallerService.callSecondSmsService(number, body);
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                smsLoggerInterface.logSms(number, body, SmsStatus.FAILED);
                LOGGER.warn("sending sms is failed");
                throw new SmsNotSentException("sending sms is failed!");
            }
        } catch (Exception ex) {
            smsLoggerInterface.logSms(number, body, SmsStatus.FAILED);
            LOGGER.warn("exception: {}", ex.getMessage());
            throw ex;
        }
        smsLoggerInterface.logSms(number, body, SmsStatus.SENT);
        LOGGER.info("tryToSendBySecondApi(), finished");

    }
}
