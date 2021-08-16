package com.smartech.smssender.repository;

import com.smartech.smssender.constant.SmsStatus;
import com.smartech.smssender.entity.SmsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsLogRepository extends JpaRepository<SmsLog, Long> {

    @Query("select s from SmsLog s  where s.status=?1 and (s.retriesNumber <?2 or s.retriesNumber is null)")
    List<SmsLog> findSmsLogByStatus(SmsStatus status, int retriesNumber);
}
