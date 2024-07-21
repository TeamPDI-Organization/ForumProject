package com.example.forumproject.helpers;

import com.example.forumproject.models.User;
import com.example.forumproject.models.UserDto;

public class UserMapper {
    public static User tryParseUserFromUserDto(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
