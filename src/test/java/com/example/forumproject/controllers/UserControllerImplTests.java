package com.example.forumproject.controllers;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.UpdateUserMapper;
import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.UpdateUserDto;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserDto;
import com.example.forumproject.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerImplTests {
    private static final String AUTHORIZATION_ACCESS_MESSAGE = "Only admins or moderators can access user's information";

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationHelper authenticationHelper;
    @Mock
    private UpdateUserMapper updateUserMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testGetUsers_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);
        when(userService.getUsers()).thenReturn(List.of(new User(), new User()));

        List<User> users = userController.getUsers(headers);

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userService).getUsers();
    }

    @Test
    public void testGetUsers_Unauthorized() {
        User regularUser = Helpers.createMockUser();
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(regularUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.getUsers(headers);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
        assertEquals("Only admins can access user's information", exception.getReason());
        verify(userService, never()).getUsers();
    }

    @Test
    public void testGetById_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();
        User user = new User();
        user.setId(1);

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);
        when(userService.getById(1)).thenReturn(user);

        User result = userController.getById(headers, 1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(userService).getById(1);
    }

    @Test
    public void testGetById_NotFound() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);
        when(userService.getById(1)).thenThrow(new EntityNotFoundException("User", 1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.getById(headers, 1);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with id 1 not found.", exception.getReason());
        verify(userService).getById(1);
    }


    @Test
    public void testBlockUser_Success(){
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);

        assertDoesNotThrow(() -> userController.blockUser(headers, 200));
        verify(userService).blockUser(200, adminUser);
    }

    @Test
    public void testBlockUser_Unauthorized() {
        User regularUser = Helpers.createMockUser();
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(regularUser);
        doThrow(new AuthorizationException(AUTHORIZATION_ACCESS_MESSAGE)).when(authenticationHelper).tryGetUser(headers);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.blockUser(headers, 200);
        });
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals(AUTHORIZATION_ACCESS_MESSAGE, exception.getReason());
        verify(userService, never()).blockUser(anyInt(), any(User.class));
    }

    @Test
    public void testUnblockUser_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);

        assertDoesNotThrow(() -> userController.unblockUser(headers, 200));
        verify(userService).unblockUser(200, adminUser);
    }

    @Test
    public void testUnblockUser_Unauthorized() {
        User regularUser = Helpers.createMockUser();
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(regularUser);
        doThrow(new AuthorizationException(AUTHORIZATION_ACCESS_MESSAGE)).when(authenticationHelper).tryGetUser(headers);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.unblockUser(headers, 200);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        assertEquals(AUTHORIZATION_ACCESS_MESSAGE, exception.getReason());
        verify(userService, never()).unblockUser(anyInt(), any(User.class));
    }
    @Test
    public void testGetPhoneNumberByUserId_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();
        PhoneNumber phoneNumber = new PhoneNumber();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);
        when(userService.getPhoneNumber(1)).thenReturn(phoneNumber);

        PhoneNumber result = userController.getPhoneNumberByUserId(headers, 1);

        assertNotNull(result);
        verify(userService).getPhoneNumber(1);
    }
    @Test
    public void testUpdate_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();
        UpdateUserDto updateUserDto = new UpdateUserDto();
        User userToUpdate = new User();
        userToUpdate.setId(1);

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);
        when(updateUserMapper.fromDto(1, updateUserDto)).thenReturn(userToUpdate);
        when(userService.update(userToUpdate, adminUser)).thenReturn(userToUpdate);

        User result = userController.update(headers, 1, updateUserDto);

        assertNotNull(result);
        verify(userService).update(userToUpdate, adminUser);
    }

    @Test
    public void testUpdateUser_NotFound() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();

        UpdateUserDto updateUserDto = new UpdateUserDto();
        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);
        when(updateUserMapper.fromDto(anyInt(), any(UpdateUserDto.class))).thenReturn(new User());
        when(userService.update(any(User.class), any(User.class))).thenThrow(new EntityNotFoundException("User", 1));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            userController.update(headers, 1, updateUserDto);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User with id 1 not found.", exception.getReason());
        verify(userService).update(any(User.class), any(User.class));
    }
    @Test
    public void testDelete_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(adminUser);

        assertDoesNotThrow(() -> userController.delete(headers, 1));
        verify(userService).delete(1, adminUser);
    }
}
