package com.example.forumproject.models;

import org.checkerframework.common.aliasing.qual.Unique;

public class UpdateUserDto {

    private String firstName;

    private String lastName;

    @Unique
    private String email;

    public UpdateUserDto() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public @Unique String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
