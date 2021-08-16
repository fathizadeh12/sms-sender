package com.smartech.smssender.service;


import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.entity.SmsLog;
import com.smartech.smssender.exception.SmsNotSentException;
import com.smartech.smssender.model.sms.SmsResponseModel;
import com.smartech.smssender.repository.SmsLogRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.http.*;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.runner.RunWith;
import org.junit.Test;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsServiceTest {

    @Autowired
    SmsServiceInterface smsServiceInterface;

    @MockBean
    SmsLogRepository smsLogRepository;

    @MockBean
    SmsLogger smsLogger;

    @MockBean
    ExternalSmsApiCallerService externalSmsApiCallerService;

    @Test(expected = SmsNotSentException.class)
    public void whenAnySmsApisDoesNotWorks_send_shouldReturnSuccessMessage() throws Exception {

        //        Given
        String number = "0912", body = "sms context";
        SmsStatus status = SmsStatus.SENT;
        SmsLog smsLog = new SmsLog(1L, number, body, new Date(), new Date(), status, null);

        when(smsLogRepository.save(any(SmsLog.class))).thenReturn(smsLog);
        when(smsLogger.logSms(any(String.class), any(String.class), any(SmsStatus.class))).thenReturn(smsLog);

        when(externalSmsApiCallerService.callFirstSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_GATEWAY));

        when(externalSmsApiCallerService.callSecondSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_GATEWAY));


//        When
        smsServiceInterface.send(number, body);

//        Then
//        should throw SmsNotSentException

    }

    @Test
    public void whenFirstSmsApiWorks_send_shouldReturnSuccessMessage() throws Exception {

        //        Given
        String number = "0912", body = "sms context";
        SmsStatus status = SmsStatus.SENT;
        SmsLog smsLog = new SmsLog(1L, number, body, new Date(), new Date(), status, null);

        when(smsLogRepository.save(any(SmsLog.class))).thenReturn(smsLog);
        when(smsLogger.logSms(any(String.class), any(String.class), any(SmsStatus.class))).thenReturn(smsLog);


        when(externalSmsApiCallerService.callFirstSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        when(externalSmsApiCallerService.callSecondSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_GATEWAY));

//        When
        SmsResponseModel smsResponseModel = smsServiceInterface.send(number, body);

//        Then
        assert ("Sms hase been sent".equals(smsResponseModel.getMessage()));

    }


    @Test
    public void whenSecondSmsApiWorks_send_shouldReturnSuccessMessage() throws Exception {

        //        Given
        String number = "0912", body = "sms context";
        SmsStatus status = SmsStatus.SENT;
        SmsLog smsLog = new SmsLog(1L, number, body, new Date(), new Date(), status, null);

        when(smsLogRepository.save(any(SmsLog.class))).thenReturn(smsLog);
        when(smsLogger.logSms(any(String.class), any(String.class), any(SmsStatus.class))).thenReturn(smsLog);


        when(externalSmsApiCallerService.callFirstSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.BAD_REQUEST));

        when(externalSmsApiCallerService.callSecondSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));


//        When
        SmsResponseModel smsResponseModel = smsServiceInterface.send(number, body);

//        Then
        assert ("Sms hase been sent".equals(smsResponseModel.getMessage()));

    }


    @Test
    public void whenBothOfSmsApisWork_send_shouldReturnSuccessMessage() throws Exception {

        //        Given
        String number = "0912", body = "sms context";
        SmsStatus status = SmsStatus.SENT;
        SmsLog smsLog = new SmsLog(1L, number, body, new Date(), new Date(), status, null);

        when(smsLogRepository.save(any(SmsLog.class))).thenReturn(smsLog);
        when(smsLogger.logSms(any(String.class), any(String.class), any(SmsStatus.class))).thenReturn(smsLog);


        when(externalSmsApiCallerService.callFirstSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        when(externalSmsApiCallerService.callSecondSmsService(any(String.class), any(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));


//        When
        SmsResponseModel smsResponseModel = smsServiceInterface.send(number, body);

//        Then
        assert ("Sms hase been sent".equals(smsResponseModel.getMessage()));

    }


}
