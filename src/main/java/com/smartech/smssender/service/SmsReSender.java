package com.smartech.smssender.service;

import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.entity.SmsLog;
import com.smartech.smssender.repository.SmsLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
public class SmsReSender {
    final SmsLogRepository smsLogRepository;

    final SmsLoggerInterface smsLoggerInterface;

    @Value("${number.of.repeat.try}")
    private int NUMBER_OF_REPEAT_TRY;

    final RestTemplate restTemplate;


    final ExternalSmsApiCallerService externalSmsApiCallerService;

    public SmsReSender(SmsLogRepository smsLogRepository, SmsLoggerInterface smsLoggerInterface, RestTemplate restTemplate, ExternalSmsApiCallerService externalSmsApiCallerService) {
        this.smsLogRepository = smsLogRepository;
        this.smsLoggerInterface = smsLoggerInterface;
        this.restTemplate = restTemplate;
        this.externalSmsApiCallerService = externalSmsApiCallerService;
    }


    @Scheduled(fixedRateString = "${retry.to.pay.duration}")
    private void reSendSms() throws Exception {
        List<SmsLog> smsLogList = smsLogRepository.findSmsLogByStatus(SmsStatus.FAILED, NUMBER_OF_REPEAT_TRY);
        for (SmsLog smsLog : smsLogList) {
            reSend(smsLog);
        }
    }

    public void reSend(SmsLog smsLog) {

        ResponseEntity<?> responseEntity;
        Date date = new Date();


        try {
            responseEntity = externalSmsApiCallerService.callFirstSmsService(smsLog.getNumber(), smsLog.getBody());

            if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                smsLoggerInterface.updateSmsLog(smsLog, date, SmsStatus.SENT);

            } else {
                reTryToSendBySecondApi(smsLog, date);
            }

        } catch (Exception e) {
            reTryToSendBySecondApi(smsLog, date);
        }

    }

    private void reTryToSendBySecondApi(SmsLog smsLog, Date date) {
        ResponseEntity<?> responseEntity;
        try {
            responseEntity = externalSmsApiCallerService.callSecondSmsService(smsLog.getNumber(), smsLog.getBody());
            if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
                smsLoggerInterface.updateSmsLog(smsLog, date, SmsStatus.FAILED);
                return;
            }
        } catch (Exception ex) {
            smsLoggerInterface.updateSmsLog(smsLog, date, SmsStatus.FAILED);
            throw ex;
        }
        smsLoggerInterface.updateSmsLog(smsLog, date, SmsStatus.SENT);
    }
}
