/*package com.example.attendance.controller;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.service.AttendanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    private static final double INSTITUTE_LATITUDE = 14.5460;
    private static final double INSTITUTE_LONGITUDE = 77.4550;

    // Endpoint for adding attendance
    @PostMapping("/add")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceData attendanceData) {
        // Fetch the user from the database using userId from attendanceData
        System.out.println("Marking attendance: " + attendanceData);
        User user = userRepository.findById(attendanceData.getUser().getId()).orElse(null);

        if (user != null) {
            // Ensure required fields are present
            if (attendanceData.getLoginOption() == null || attendanceData.getLoginOption().isEmpty()) {
                return ResponseEntity.status(400).body("Missing required field: loginOption");
            }

            // Set default user latitude and longitude to match the institute's location
            attendanceData.setUserLatitude(INSTITUTE_LATITUDE);
            attendanceData.setUserLongitude(INSTITUTE_LONGITUDE);

            // Set the user and current date details
            attendanceData.setUser(user);
            attendanceData.setLoginTime(new Date());
            

            // Save attendance data
            attendanceService.saveAttendance(attendanceData);
            return ResponseEntity.ok("Attendance marked successfully.");
        }

        return ResponseEntity.status(404).body("User not found.");
    }
}
*/
package com.example.attendance.controller;

import com.example.attendance.model.AttendanceData;
import com.example.attendance.model.User;
import com.example.attendance.repository.UserRepository;
import com.example.attendance.service.AttendanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    private static final double INSTITUTE_LATITUDE = 14.5460;
    private static final double INSTITUTE_LONGITUDE = 77.4550;

    // Endpoint for adding attendance
    @PostMapping("/add")
    public ResponseEntity<String> markAttendance(@RequestBody AttendanceData attendanceData) {
        System.out.println("Marking attendance: " + attendanceData);

        Optional<User> userOpt = userRepository.findById(attendanceData.getUser().getId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();
        String loginOption = attendanceData.getLoginOption();

        if (loginOption == null || loginOption.isEmpty()) {
            return ResponseEntity.status(400).body("Missing required field: loginOption");
        }

        // Check if lunch or tea is already marked for today
        boolean alreadyMarked = attendanceService.hasMarkedAttendanceToday(user, loginOption);
        if ((loginOption.equalsIgnoreCase("lunch") && alreadyMarked) ||
                (loginOption.equalsIgnoreCase("tea") && alreadyMarked)) {
            return ResponseEntity.status(400).body(loginOption + " can only be marked once per day.");
        }

        // Set user location to institute's location
        attendanceData.setUserLatitude(INSTITUTE_LATITUDE);
        attendanceData.setUserLongitude(INSTITUTE_LONGITUDE);

        // Set user, login time, and current date details for attendance data
        attendanceData.setUser(user);
        attendanceData.setLoginTime(new Date());

        // Store logout option data in the database as well
        if (loginOption.equalsIgnoreCase("logout")) {
            attendanceService.saveAttendance(attendanceData);
            return ResponseEntity.ok("User logged out successfully and logout data stored.");
        }

        // Save attendance data
        attendanceService.saveAttendance(attendanceData);
        return ResponseEntity.ok("Attendance marked successfully.");
    }

    // Endpoint to check if lunch or tea is already marked for the day
    @GetMapping("/status")
    public ResponseEntity<?> getAttendanceStatus(@RequestParam Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        boolean lunchMarked = attendanceService.hasMarkedAttendanceToday(userOpt.get(), "lunch");
        boolean teaMarked = attendanceService.hasMarkedAttendanceToday(userOpt.get(), "tea");

        return ResponseEntity.ok().body(Map.of(
                "lunchMarked", lunchMarked,
                "teaMarked", teaMarked));
    }

}
