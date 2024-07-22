package com.example.forumproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "admin_phone_numbers")
public class AdminPhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "phone_number")
    @Size(min = 8, max = 13, message = "Phone number must be between 8 and 13 symbols")
    private String phoneNumber;

    @Column(name = "admin_id")
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

    public AdminPhoneNumber() {

    }

    public AdminPhoneNumber(int id, String phoneNumber, User user) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
