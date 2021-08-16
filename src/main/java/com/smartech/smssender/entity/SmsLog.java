package com.smartech.smssender.entity;

import com.smartech.smssender.constant.SmsStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SMS_LOG")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SmsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private String number;


    @Column(name = "body")
    private String body;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_try_date")
    private Date lastTryDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SmsStatus status;

    @Column(name = "retries_number")
    private Integer retriesNumber;

}


