package com.example.attendance.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class AttendanceData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Date loginTime;
    

    private String loginOption; // e.g., "tea", "lunch", "breakfast", "exit"
    private Double instituteLatitude;
    private Double instituteLongitude;
    private Double userLatitude;
    private Double userLongitude;

    private String instituteName; // New field for institute name

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    
    

    public String getLoginOption() {
        return loginOption;
    }

    public void setLoginOption(String loginOption) {
        this.loginOption = loginOption;
    }

    public Double getInstituteLatitude() {
        return instituteLatitude;
    }

    public void setInstituteLatitude(Double instituteLatitude) {
        this.instituteLatitude = instituteLatitude;
    }

    public Double getInstituteLongitude() {
        return instituteLongitude;
    }

    public void setInstituteLongitude(Double instituteLongitude) {
        this.instituteLongitude = instituteLongitude;
    }

    public Double getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(Double userLatitude) {
        this.userLatitude = userLatitude;
    }

    public Double getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(Double userLongitude) {
        this.userLongitude = userLongitude;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }
}
