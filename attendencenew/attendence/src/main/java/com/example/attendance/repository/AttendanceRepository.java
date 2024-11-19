package com.example.attendance.repository;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<AttendanceData, Long> {

    boolean existsByUserAndLoginOptionAndLoginTimeAfter(User user, String loginOption, Date startOfDay);
    List<AttendanceData> findByUserId(Long userId);
}
