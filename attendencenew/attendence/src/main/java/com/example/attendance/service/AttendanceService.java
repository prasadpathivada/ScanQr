package com.example.attendance.service;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;
import com.example.attendance.repository.AttendanceRepository;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public void saveAttendance(AttendanceData attendanceData) {
        attendanceRepository.save(attendanceData);
    }

    public boolean hasMarkedAttendanceToday(User user, String loginOption) {
        // Assume this method checks if the user has already marked lunch or tea
        // attendance today
        // Implement logic to query the AttendanceRepository to verify if lunch or tea
        // has been marked today
        Date startOfDay = getStartOfDay(); // Helper method to get start of today
        return attendanceRepository.existsByUserAndLoginOptionAndLoginTimeAfter(user, loginOption, startOfDay);
    }

    private Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Date(calendar.getTimeInMillis());
        // Helper method to return the start of the day timestamp
        // Implement this based on your specific date handling requirements
    }

    public List<AttendanceData> findAllAttendanceRecords() {
        // TODO Auto-generated method stub
        return attendanceRepository.findAll();
    }

    public List<AttendanceData> findAttendanceRecordsByUserId(Long id) {
        // TODO Auto-generated method stub
        return attendanceRepository.findByUserId(id);

    }
}
