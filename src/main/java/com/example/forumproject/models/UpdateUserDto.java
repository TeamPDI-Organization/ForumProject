package com.example.forumproject.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.checkerframework.common.aliasing.qual.Unique;

public class UpdateUserDto {

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Email
    @Unique
    private String email;

    @NotNull
    private String password;

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
}
