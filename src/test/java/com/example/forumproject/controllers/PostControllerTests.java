package com.example.forumproject.controllers;

import com.example.forumproject.Helpers;
import com.example.forumproject.controllers.RestControllers.PostController;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostDto;
import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.User;
import com.example.forumproject.services.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostControllerTests {

    @Mock
    private PostService postService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private AuthenticationHelper authenticationHelper;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPosts() {
        PostFilterOptions options = new PostFilterOptions("Mock Post Random Title", "date", "asc");
        List<Post> expectedPosts = Collections.singletonList(Helpers.createMockPost());

        when(postService.getPosts(any(PostFilterOptions.class))).thenReturn(expectedPosts);

        List<Post> actualPosts = postController.getPosts("Mock Post Random Title", "date", "asc");

        ArgumentCaptor<PostFilterOptions> captor = ArgumentCaptor.forClass(PostFilterOptions.class);
        verify(postService).getPosts(captor.capture());
        assertEquals(expectedPosts, actualPosts);

        PostFilterOptions capturedOptions = captor.getValue();
        assertEquals(options.getTitle(), capturedOptions.getTitle());
        assertEquals(options.getSortBy(), capturedOptions.getSortBy());
        assertEquals(options.getSortOrder(), capturedOptions.getSortOrder());
    }

    @Test
    void testCreatePost_Success() {

        User user = Helpers.createMockUser();
        PostDto postDto = new PostDto();
        Post post = Helpers.createMockPost();

        when(authenticationHelper.tryGetUser(any(HttpHeaders.class))).thenReturn(user);
        when(postMapper.fromDto(postDto)).thenReturn(post);
        when(postService.create(post, user)).thenReturn(post);

        Post actualPost = postController.createPost(new HttpHeaders(), postDto);

        assertEquals(post, actualPost);
        verify(postService).create(post, user);
    }

    @Test
    void testAddLikeToPost_Success() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();

        when(authenticationHelper.tryGetUser(any(HttpHeaders.class))).thenReturn(user);
        when(postService.getPostById(post.getId())).thenReturn(post);
        when(postService.addLike(post, user)).thenReturn(post);

        Post actualPost = postController.addLikeToPost(new HttpHeaders(), post.getId());

        assertEquals(post, actualPost);
        verify(postService).addLike(post, user);
    }

    @Test
    void testUpdatePost_Success(){
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        PostDto postDto = new PostDto();

        when(authenticationHelper.tryGetUser(any(HttpHeaders.class))).thenReturn(user);
        when(postMapper.fromDto(post.getId(), postDto)).thenReturn(post);
        when(postService.update(post, user)).thenReturn(post);

        Post actualPost = postController.update(new HttpHeaders(), post.getId(), postDto);

        assertEquals(post, actualPost);
        verify(postService).update(post, user);
    }

    @Test
    void testUpdatePost_NotFound(){
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        PostDto postDto = new PostDto();

        post.setId(1);
        when(authenticationHelper.tryGetUser(any(HttpHeaders.class))).thenReturn(user);
        when(postMapper.fromDto(post.getId(), postDto)).thenReturn(post);
        when(postService.update(post, user)).thenThrow(new EntityNotFoundException("Post", post.getId()));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            postController.update(new HttpHeaders(), post.getId(), postDto);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Post with id 1 not found.", exception.getReason());
        verify(postService).update(post, user);


    }

    @Test
    void testDeletePost_Success(){
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();

        when(authenticationHelper.tryGetUser(any(HttpHeaders.class))).thenReturn(user);
        doNothing().when(postService).delete(post.getId(), user);

        assertDoesNotThrow(() -> postController.delete(new HttpHeaders(), post.getId()));
        verify(postService).delete(post.getId(), user);
    }

    @Test
    void testGetTopCommentedPosts() {
        List<Post> expectedPosts = Collections.singletonList(Helpers.createMockPost());
        when(postService.getTopCommentedPosts()).thenReturn(expectedPosts);
        List<Post> actualPosts = postController.getTopCommentedPosts();

        assertEquals(expectedPosts, actualPosts);
        verify(postService).getTopCommentedPosts();
    }

    @Test
    void testGetRecentPosts(){
        List<Post> expectedPosts = Collections.singletonList(Helpers.createMockPost());
        when(postService.getRecentPosts()).thenReturn(expectedPosts);
        List<Post> actualPosts = postController.getRecentPosts();

        assertEquals(expectedPosts, actualPosts);
        verify(postService).getRecentPosts();
    }
}
