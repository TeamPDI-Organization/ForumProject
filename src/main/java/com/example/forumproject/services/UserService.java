package com.example.forumproject.services;

import com.example.forumproject.models.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getById(int id);

    User getByUsername(String username);

    User create(User user);

    User update(int id, User user2);

    void delete(int id, User user);
}
