package com.smartech.smssender.service;

import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.entity.SmsLog;

import java.util.Date;

public interface SmsLoggerInterface {

    public SmsLog logSms(String number, String body, SmsStatus status);
    public SmsLog updateSmsLog(SmsLog smsLog, Date lastTryDate, SmsStatus status);
}
