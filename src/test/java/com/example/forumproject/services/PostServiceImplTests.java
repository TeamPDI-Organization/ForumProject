package com.example.forumproject.services;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create_ValidPost_Success() {
        User user = new User();
        Post post = new Post();
        post.setTitle("New Post Title");

        when(postRepository.getByTitle(post.getTitle())).thenThrow(EntityNotFoundException.class);

        assertDoesNotThrow(() -> postService.create(post, user));
        verify(postRepository).create(post);
    }

    @Test
    void create_DuplicateTitle_ThrowsEntityDuplicateException() {
        User user = new User();
        Post post = new Post();
        post.setTitle("Existing Post Title");

        when(postRepository.getByTitle(post.getTitle())).thenReturn(new Post());

        assertThrows(EntityDuplicateException.class, () -> postService.create(post, user));
    }

    @Test
    void checkModifyPermissions_AdminUser_Success() {
        User adminUser = new User();
        adminUser.setAdmin(true);
        Post post = new Post();
        post.setCreatedBy(adminUser);

        assertDoesNotThrow(() -> postService.checkModifyPermissions(post.getId(), adminUser));
    }
}
