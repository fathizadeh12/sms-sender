package com.smartech.smssender.service;

import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.entity.SmsLog;
import com.smartech.smssender.repository.SmsLogRepository;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.Test;

import java.util.Date;


@RunWith(MockitoJUnitRunner.class)
public class SmsLoggerTest {

    @Mock
    SmsLogRepository smsLogRepository;

    @InjectMocks
    SmsLogger smsLogger;

    @Test
    public void logSmsTest() {
//        Given
        String number = "0912", body = "sms context";
        SmsStatus status = SmsStatus.SENT;
        SmsLog smsLog = new SmsLog(1L, number, body, new Date(), new Date(), status, null);

        when(smsLogRepository.save(any(SmsLog.class))).thenReturn(smsLog);

//        When
        SmsLog result = smsLogger.logSms(number, body, status);

//    Then
        assert (result.getNumber().equals(number));
        assert (result.getBody().equals(body));
        assert (result.getStatus().equals(SmsStatus.SENT));
        assert (result.getCreateDate() != null);

    }


    @Test
    public void updateSmsLogTest() {
//        Given
        String number = "0912", body = "sms context";
        SmsStatus status = SmsStatus.SENT;
        SmsLog smsLog = new SmsLog(1L, number, body, new Date(), new Date(), status,1 );

        when(smsLogRepository.save(any(SmsLog.class))).thenReturn(smsLog);

//        When
        SmsLog result = smsLogger.updateSmsLog(smsLog, new Date(), status);

//    Then
        assert (result.getNumber().equals(number));
        assert (result.getBody().equals(body));
        assert (result.getStatus().equals(SmsStatus.SENT));
        assert (result.getCreateDate() != null);
        assert (result.getLastTryDate() != null);

    }



}
