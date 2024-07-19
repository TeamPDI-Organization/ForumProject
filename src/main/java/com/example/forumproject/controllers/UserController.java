package com.example.forumproject.controllers;

import com.example.forumproject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    public UserController() {
    }

    @GetMapping
    public List<User> getUsers() {

    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {

    }

}
