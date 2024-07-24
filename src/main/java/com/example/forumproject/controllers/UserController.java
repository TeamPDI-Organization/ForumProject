package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.UpdateUserMapper;
import com.example.forumproject.helpers.UserMapper;
import com.example.forumproject.models.UpdateUserDto;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserDto;
import com.example.forumproject.models.UserFilterOptions;
import com.example.forumproject.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final String GET_USERS_ERROR_MESSAGE = "Only admins can access user's information";

    private final UserService userService;

    private final UserMapper userMapper;

    private final UpdateUserMapper updateUserMapper;

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserController(UserService userService,
                          UserMapper userMapper,
                          UpdateUserMapper updateUserMapper,
                          AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.updateUserMapper = updateUserMapper;
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
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = userMapper.fromDto(userDto);
        try {
            userService.create(user);
            return userDto;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UpdateUserDto updateUserDto) {
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

    private void isAdminOrModerator(User user) {
        if (!user.isAdmin() && !user.isModerator()) {
            throw new AuthorizationException(GET_USERS_ERROR_MESSAGE);
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
