package com.example.forumproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "phone_number")
    private String phoneNumber;

    public PhoneNumber() {

    }

    public PhoneNumber(int id, String phoneNumber) {
        this.userId = id;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
