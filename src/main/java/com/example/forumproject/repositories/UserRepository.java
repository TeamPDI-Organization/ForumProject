package com.example.forumproject.repositories;

import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> getUsers();

    User getById(int id);

    User getByUsername(String username);

    User create(User user);

    void delete(int id);

    User update(User user);

    List<User> searchUsers(UserFilterOptions options);

    PhoneNumber setPhoneNumber(PhoneNumber phoneNumber);

    PhoneNumber getPhoneNumber(int userId);

    User makeModerator(int userId);
}
