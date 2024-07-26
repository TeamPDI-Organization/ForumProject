package com.example.forumproject.controllers;

import com.example.forumproject.Helpers;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
