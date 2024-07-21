package com.example.forumproject.repositories;

import com.example.forumproject.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getUsers();

    User getById(int id);

    User getByUsername(String username);

    User create(User user);
}
