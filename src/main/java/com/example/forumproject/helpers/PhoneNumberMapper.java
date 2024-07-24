package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.PhoneNumberDto;
import com.example.forumproject.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {

    private UserService userService;

    public PhoneNumberMapper(UserService userService) {
        this.userService = userService;
    }

    public PhoneNumber fromDto(int userId, PhoneNumberDto phoneNumberDto) {
        if (userService.getPhoneNumber(userId) != null) {
            throw new EntityDuplicateException("User", "phone number", phoneNumberDto.getPhoneNumber());
        }

        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setUserId(userId);
        phoneNumber.setPhoneNumber(phoneNumberDto.getPhoneNumber());

        return phoneNumber;
    }
}
