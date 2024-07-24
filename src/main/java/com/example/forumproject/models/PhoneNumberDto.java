package com.example.forumproject.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

public class PhoneNumberDto {

    @NotNull
    @Unique
    @Size(min = 8, max = 13, message = "Phone number must be between 8 and 13 symbols")
    private String phoneNumber;

    public PhoneNumberDto() {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
