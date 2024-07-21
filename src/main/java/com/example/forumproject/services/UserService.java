package com.example.forumproject.services;

import com.example.forumproject.models.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getById(int id);

    User getByUsername(String username);
}
