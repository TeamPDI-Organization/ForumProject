package com.example.forumproject.helpers;

import com.example.forumproject.models.UpdateUserDto;
import com.example.forumproject.models.User;
import com.example.forumproject.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserMapper {

    private UserService userService;

    @Autowired
    public UpdateUserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(int id, UpdateUserDto updateUserDto) {
        User user = userService.getById(id);
        user.setFirstName(updateUserDto.getFirstName());
        user.setLastName(updateUserDto.getLastName());
        user.setEmail(updateUserDto.getEmail());

        return user;
    }

}
