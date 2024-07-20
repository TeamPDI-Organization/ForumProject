package com.example.forumproject.repositories;

import com.example.forumproject.models.User;

import java.util.List;

public interface UserRepository {
    List<User> getUsers();
}
