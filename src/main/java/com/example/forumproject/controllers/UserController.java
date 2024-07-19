package com.example.forumproject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
