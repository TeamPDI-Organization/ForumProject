package com.example.forumproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumber {

    @Id
    @OneToOne
    @JoinColumn(name = "admin_id")
    private User user;

    @Column(name = "phone_number")
    @Size(min = 8, max = 13, message = "Phone number must be between 8 and 13 symbols")
    private String phoneNumber;

    public PhoneNumber() {

    }

    public PhoneNumber(User user, String phoneNumber) {
        this.user = user;
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
