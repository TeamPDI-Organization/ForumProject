package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
import com.example.forumproject.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import java.util.Collections;
import java.util.List;

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
        User user = Helpers.createMockUser();

        when(mockUserRepository.getByUsername(user.getUsername())).thenThrow(EntityNotFoundException.class);
        when(mockUserRepository.create(user)).thenReturn(user);

        User createdUser = userService.create(user);
        assertEquals(user, createdUser);
        verify(mockUserRepository).getByUsername(user.getUsername());
        verify(mockUserRepository).create(user);

    }

    @Test
    void testCreateUser_DuplicateUsername() {
        User user = Helpers.createMockUser();

        when(mockUserRepository.getByUsername(user.getUsername())).thenReturn(user);

        assertThrows(EntityDuplicateException.class, () -> userService.create(user));

        verify(mockUserRepository).getByUsername(user.getUsername());
        verify(mockUserRepository, never()).create(user);
    }

    @Test
    void testDeleteUser_Success() {
        User userToDelete = Helpers.createMockUser();
        int userId = userToDelete.getId();

        when(mockUserRepository.getById(userId)).thenReturn(userToDelete);

        userService.delete(userId, userToDelete);

        verify(mockUserRepository).delete(userId);
    }

    @Test
    void testDeleteUser_Unauthorized() {
        User user = Helpers.createMockUser();
        User userToDelete = Helpers.createMockUser();
        userToDelete.setId(101);
        int userToDeleteId = userToDelete.getId();

        when(mockUserRepository.getById(userToDeleteId)).thenReturn(userToDelete);

        assertThrows(AuthorizationException.class, () -> userService.delete(userToDeleteId, user));

        verify(mockUserRepository, never()).delete(userToDeleteId);
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

    @Test
    public void testSearchUsers(){
        UserFilterOptions options = mock(UserFilterOptions.class);
        List<User> expectedUsers = Collections.singletonList(mock(User.class));

        when(mockUserRepository.searchUsers(options)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.searchUsers(options);

        assertEquals(expectedUsers, actualUsers);
        verify(mockUserRepository).searchUsers(options);
    }

    @Test
    public void testGetUsers(){
        List<User> expectedUsers = Collections.singletonList(mock(User.class));

        when(mockUserRepository.getUsers()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsers();

        assertEquals(expectedUsers, actualUsers);
        verify(mockUserRepository).getUsers();
    }

    @Test
    public void testGetById(){

        int userId = 100;
        User user = Helpers.createMockUser();

        when(mockUserRepository.getById(userId)).thenReturn(user);

        User actualUser = userService.getById(user.getId());

        assertEquals(user, actualUser);
        verify(mockUserRepository).getById(userId);
    }

    @Test
    public void testGetByUsername(){

        String username = "mockUsername";

        User expectedUser = Helpers.createMockUser();
        when(mockUserRepository.getByUsername(username)).thenReturn(expectedUser);

        User actualUser = userService.getByUsername(username);

        assertEquals(expectedUser, actualUser);
        verify(mockUserRepository).getByUsername(username);
    }
    @Test
    void testSetPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneNumber("123-456-7890");
        when(mockUserRepository.setPhoneNumber(phoneNumber)).thenReturn(phoneNumber);

        PhoneNumber result = userService.setPhoneNumber(phoneNumber);

        assertEquals(phoneNumber, result);
        verify(mockUserRepository).setPhoneNumber(phoneNumber);
    }

    @Test
    void testGetPhoneNumber() {
        int userId = 1;
        PhoneNumber expectedPhoneNumber = new PhoneNumber();
        expectedPhoneNumber.setPhoneNumber("123-456-7890");
        when(mockUserRepository.getPhoneNumber(userId)).thenReturn(expectedPhoneNumber);

        PhoneNumber actualPhoneNumber = userService.getPhoneNumber(userId);

        assertEquals(expectedPhoneNumber, actualPhoneNumber);
        verify(mockUserRepository).getPhoneNumber(userId);
    }
}
