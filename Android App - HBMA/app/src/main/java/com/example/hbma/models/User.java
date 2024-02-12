package com.example.hbma.models;

public class User {
    String time,exceed;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExceed() {
        return exceed;
    }

    public void setExceed(String exceed) {
        this.exceed = exceed;
    }

    String profilepic,username,email,password,userId,relativeNo,phoneNo;
    String bloodPressure, location;

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User(String profilepic, String username, String email, String userId, String relativeNo, String phoneNo) {
        this.profilepic = profilepic;
        this.username = username;
        this.email = email;
        this.userId = userId;
        this.relativeNo = relativeNo;
        this.phoneNo = phoneNo;
    }

    public User(String bloodPressure, String location) {
        this.bloodPressure = bloodPressure;
        this.location = location;
    }

    public User(){}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRelativeNo() {
        return relativeNo;
    }

    public void setRelativeNo(String relativeNo) {
        this.relativeNo = relativeNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
