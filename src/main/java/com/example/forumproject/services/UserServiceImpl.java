package com.example.forumproject.services;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.FileLimitationsException;
import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
import com.example.forumproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private static final String GET_USERS_ERROR_MESSAGE = "Only admins or moderators can access user's information";;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> searchUsers(UserFilterOptions options) {
        return userRepository.searchUsers(options);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.getUsers();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User create(User user) {
        boolean duplicateExists = true;

        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        return userRepository.create(user);
    }

    @Override
    public void delete(int id, User user) {
        User existingUser = userRepository.getById(id);
        if (!(user.isAdmin() || existingUser.equals(user))) {
            throw new AuthorizationException("Only admin or the user themselves can delete the user.");
        }

        userRepository.delete(id);
    }

    @Override
    public User update(User user, User currentUser) {
        if (currentUser.getId() != user.getId()) {
            throw new AuthorizationException("Only the user themselves can update the user.");
        }

        boolean duplicateExists = true;
        try {
            User existingUser = userRepository.getByUsername(currentUser.getUsername());
            if (existingUser.getId() == currentUser.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        return userRepository.update(user);
    }
    public void blockUser(int userId, User currentUser) {
        isAdminOrModerator(currentUser);
        User user = userRepository.getById(userId);

        user.setBlocked(true);
        userRepository.update(user);
    }

    public void unblockUser(int userId, User currentUser) {
        isAdminOrModerator(currentUser);
        User user = userRepository.getById(userId);

        user.setBlocked(false);
        userRepository.update(user);
    }
    private void isAdminOrModerator(User user) {
        if (!user.isAdmin() && !user.isModerator()) {
            throw new AuthorizationException(GET_USERS_ERROR_MESSAGE);
        }
    }

    @Override
    public PhoneNumber setPhoneNumber(PhoneNumber phoneNumber) {
        return userRepository.setPhoneNumber(phoneNumber);
    }

    @Override
    public PhoneNumber getPhoneNumber(int userId) {
        return userRepository.getPhoneNumber(userId);
    }

    @Override
    public User makeModerator(int userId) {
        return userRepository.makeModerator(userId);
    }


}
