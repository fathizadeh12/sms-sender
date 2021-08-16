package com.smartech.smssender.service;

import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.entity.SmsLog;
import com.smartech.smssender.repository.SmsLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SmsLogger implements SmsLoggerInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsLogger.class);
    final SmsLogRepository smsLogRepository;
    public SmsLogger(SmsLogRepository smsLogRepository) {
        this.smsLogRepository = smsLogRepository;
    }

    @Override
    public SmsLog logSms(String number, String body, SmsStatus status) {

        LOGGER.info("logSms(), input:  number= {}, body= {}, status: {}", number, body, status.toString());
        SmsLog smsLog = new SmsLog();
        smsLog.setNumber(number);
        smsLog.setBody(body);
        smsLog.setStatus(status);
        smsLog.setCreateDate(new Date());
        return smsLogRepository.save(smsLog);

    }

    @Override
    public SmsLog updateSmsLog(SmsLog smsLog, Date lastTryDate, SmsStatus status) {

        LOGGER.info("updateSmsLog(), input: {}, lastTryDate={},status={}", smsLog.toString(), lastTryDate, status);
        smsLog.setLastTryDate(lastTryDate);
        smsLog.setStatus(status);
        smsLog.setRetriesNumber(smsLog.getRetriesNumber() != null ? smsLog.getRetriesNumber() + 1 : 1);
        return smsLogRepository.save(smsLog);
    }
}
