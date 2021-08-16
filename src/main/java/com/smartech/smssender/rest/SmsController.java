package com.smartech.smssender.rest;

import com.smartech.smssender.model.sms.SmsResponseModel;
import com.smartech.smssender.service.SmsServiceInterface;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sms")
public class SmsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsController.class);

    final SmsServiceInterface smsServiceInterface;

    public SmsController(SmsServiceInterface smsServiceInterface) {
        this.smsServiceInterface = smsServiceInterface;
    }

    @ApiOperation(value = "send sms to input number by input body as context", httpMethod = "GET")
    @GetMapping("/send")
    private ResponseEntity<SmsResponseModel> sendSms(@RequestParam(name = "number") String number,
                                                     @RequestParam(name = "body") String body) throws Exception {

        LOGGER.info("Called url: {}","/sms/send");
        SmsResponseModel smsResponseModel = smsServiceInterface.send(number, body);

        return ResponseEntity.ok(smsResponseModel);
    }

}
