package com.example.forumproject.services;

import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<User> searchUsers(UserFilterOptions options);

    List<User> getUsers();

    User getById(int id);

    User getByUsername(String username);

    User create(User user);

    void delete(int id, User user);

    User update(User user, User currentUser);

    void blockUser(int userId, User currentUser);

    void unblockUser(int userId, User currentUser);

    PhoneNumber setPhoneNumber(PhoneNumber phoneNumber);

    PhoneNumber getPhoneNumber(int userId);

    User makeModerator(int userId);

    User removeModerator(int userId);
}
