package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.controllers.UserController;
import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
import com.example.forumproject.repositories.UserRepository;
import com.example.forumproject.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTests {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private AuthenticationHelper authenticationHelper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        User newUser = new User();
        newUser.setUsername("newUser");

        when(mockUserRepository.getByUsername(newUser.getUsername())).thenThrow(EntityNotFoundException.class);
        when(mockUserRepository.create(newUser)).thenReturn(newUser);

        User createdUser = userService.create(newUser);

        verify(mockUserRepository).getByUsername(newUser.getUsername());
        verify(mockUserRepository).create(newUser);
        // Additional assertions as needed
    }

    @Test
    void testCreateUser_DuplicateUsername() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");

        User newUser = new User();
        newUser.setUsername("existingUser");

        when(mockUserRepository.getByUsername(existingUser.getUsername())).thenReturn(existingUser);

        assertThrows(EntityDuplicateException.class, () -> userService.create(newUser));

        verify(mockUserRepository).getByUsername(existingUser.getUsername());
        verify(mockUserRepository, never()).create(newUser);
    }

    @Test
    void testDeleteUser_Success() {
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setAdmin(true);

        User existingUser = new User();
        existingUser.setId(2);

        when(mockUserRepository.getById(existingUser.getId())).thenReturn(existingUser);

        userService.delete(existingUser.getId(), currentUser);

        verify(mockUserRepository).getById(existingUser.getId());
        verify(mockUserRepository).delete(existingUser.getId());
    }

    @Test
    void testDeleteUser_Unauthorized() {
        User currentUser = new User();
        currentUser.setId(1);

        User existingUser = new User();
        existingUser.setId(2);

        when(mockUserRepository.getById(existingUser.getId())).thenReturn(existingUser);


        assertThrows(AuthorizationException.class, () -> userService.delete(existingUser.getId(), currentUser));

        verify(mockUserRepository).getById(existingUser.getId());
        verify(mockUserRepository, never()).delete(existingUser.getId());
    }

    @Test
    public void testBlockUser_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        User userToBlock = Helpers.createMockUser();
        userToBlock.setId(200);

        when(mockUserRepository.getById(200)).thenReturn(userToBlock);

        userService.blockUser(200, adminUser);

        assertTrue(userToBlock.isBlocked());
        verify(mockUserRepository).update(userToBlock);
    }

    @Test
    public void testBlockUser_Unauthorized() {
        User regularUser = Helpers.createMockUser();
        HttpHeaders headers = new HttpHeaders();

        when(authenticationHelper.tryGetUser(headers)).thenReturn(regularUser);

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            userService.blockUser(200, regularUser);
        });

        verify(mockUserRepository, never()).update(any(User.class));

    }

    @Test
    public void testUnblockUser_Success() {
        User adminUser = Helpers.createMockUser();
        adminUser.setAdmin(true);
        User userToUnblock = Helpers.createMockUser();
        userToUnblock.setId(200);
        userToUnblock.setBlocked(true);

        when(mockUserRepository.getById(200)).thenReturn(userToUnblock);

        userService.unblockUser(200, adminUser);

        assertFalse(userToUnblock.isBlocked());
        verify(mockUserRepository).update(userToUnblock);
    }

    @Test
    public void testUnblockUser_Unauthorized() {
        User regularUser = Helpers.createMockUser();
        regularUser.setAdmin(false);
        regularUser.setModerator(false);

        assertThrows(AuthorizationException.class, () -> {
            userService.unblockUser(200, regularUser);
        });

        verify(mockUserRepository, never()).update(any(User.class));
    }
}
