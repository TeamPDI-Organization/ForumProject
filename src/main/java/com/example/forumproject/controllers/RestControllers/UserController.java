package com.example.forumproject.controllers.RestControllers;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.exceptions.FileLimitationsException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.PhoneNumberMapper;
import com.example.forumproject.helpers.UpdateUserMapper;
import com.example.forumproject.helpers.UserMapper;
import com.example.forumproject.models.*;
import com.example.forumproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String GET_USERS_ERROR_MESSAGE = "Only admins can access user's information";

    private final UserService userService;

    private final UserMapper userMapper;

    private final UpdateUserMapper updateUserMapper;

    private final PhoneNumberMapper phoneNumberMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper,
                          UpdateUserMapper updateUserMapper,
                          PhoneNumberMapper phoneNumberMapper,
                          AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.updateUserMapper = updateUserMapper;
        this.phoneNumberMapper = phoneNumberMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getUsers(@RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            isAdmin(user);
            return userService.getUsers();
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public User getById(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            isAdmin(user);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PostMapping
    public RegisterDto create(@Valid @RequestBody RegisterDto registerDto) {
        User user = userMapper.fromDto(registerDto);
        try {
            userService.create(user);
            return registerDto;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/phone-number/{id}")
    public PhoneNumber getPhoneNumberByUserId(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        User user = authenticationHelper.tryGetUser(headers);
        PhoneNumber phoneNumber = userService.getPhoneNumber(id);
        if (phoneNumber == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This admin don't have phone number");
        }

        return phoneNumber;
    }

    @PostMapping("/phone-number/{id}")
    public PhoneNumberDto setPhoneNumber(@RequestHeader HttpHeaders headers, @PathVariable int id,
                               @RequestBody PhoneNumberDto phoneNumberDto) {

        User user = authenticationHelper.tryGetUser(headers);
        isAdmin(user);

        try {
            PhoneNumber newPhoneNumber = phoneNumberMapper.fromDto(id, phoneNumberDto);
            userService.setPhoneNumber(newPhoneNumber);
            return phoneNumberDto;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/moderator/{id}")
    public User makeModerator(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            isAdmin(currentUser);
            return userService.makeModerator(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id,
                       @Valid @RequestBody UpdateUserDto updateUserDto) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            User userToUpdate = updateUserMapper.fromDto(id, updateUserDto);

            return userService.update(userToUpdate, currentUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private void isAdmin(User user) {
        if (!user.isAdmin()) {
            throw new AuthorizationException(GET_USERS_ERROR_MESSAGE);
        }
    }
    @PutMapping("/{id}/block")
    public void blockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            userService.blockUser(id, currentUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PutMapping("/{id}/unblock")
    public void unblockUser(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User currentUser = authenticationHelper.tryGetUser(headers);
            userService.unblockUser(id, currentUser);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String firstName){

        UserFilterOptions options = new UserFilterOptions(username, email, firstName);

        return userService.searchUsers(options);
    }
}
