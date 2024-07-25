
package com.example.forumproject.services;

import com.example.forumproject.Helpers;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostServiceImplTests {

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
    public void testGetPosts() {
        List<Post> expectedPosts = List.of(new Post(), new Post());

        when(postRepository.getPosts(any())).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.getPosts(null);
        assertEquals(expectedPosts, actualPosts);
        verify(postRepository).getPosts(any());
    }

    @Test
    public void testGetByUserId() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        Post post2 = Helpers.createMockPost();
        post.setCreatedBy(user);
        List<Post> userPosts = List.of(post, post2);

        when(postRepository.getByUserId(user.getId())).thenReturn(userPosts);

        List<Post> actualPosts = postService.getByUserId(user.getId());

        assertEquals(userPosts, actualPosts);
        verify(postRepository).getByUserId(user.getId());

    }

    @Test
    public void testGetByTitle() {
        Post expectedPost = Helpers.createMockPost();

        when(postRepository.getByTitle(expectedPost.getTitle())).thenReturn(expectedPost);

        Post actualPost = postService.getByTitle(expectedPost.getTitle());

        assertEquals(expectedPost, actualPost);
        verify(postRepository).getByTitle(expectedPost.getTitle());
    }

    @Test
    public void testUpdate() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        post.setCreatedBy(user);

        when(postRepository.getByTitle(post.getTitle())).thenReturn(post);
        when(postRepository.getPostById(post.getId())).thenReturn(post);

        postService.update(post, user);
        verify(postRepository).update(post);
    }

    @Test
    public void testUpdateDuplicate() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        post.setCreatedBy(user);

        Post anotherPost = Helpers.createMockPost();
        anotherPost.setCreatedBy(user);
        anotherPost.setId(post.getId() + 1);

        when(postRepository.getPostById(post.getId())).thenReturn(post);
        when(postRepository.getByTitle(post.getTitle())).thenReturn(anotherPost);

        assertThrows(EntityDuplicateException.class, () -> postService.update(post, user));
    }

    @Test
    public void testDelete() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        post.setCreatedBy(user);
        when(postRepository.getPostById(post.getId())).thenReturn(post);

        postService.delete(post.getId(), user);
        verify(postRepository).delete(post.getId());
    }

    @Test
    public void testDelete_WithoutPermissions(){
        User user = Helpers.createMockUser();
        User anotherUser = Helpers.createMockUser();
        anotherUser.setId(user.getId() + 1);

        Post post = Helpers.createMockPost();
        post.setCreatedBy(anotherUser);

        when(postRepository.getPostById(post.getId())).thenReturn(post);

        assertThrows(AuthorizationException.class, () -> postService.delete(post.getId(), user));

    }

    @Test
    public void testCreatePost_ByBlockedUser(){
        User blockedUser = Helpers.createMockUser();
        blockedUser.setBlocked(true);
        Post post = Helpers.createMockPost();

        assertThrows(AuthorizationException.class, () -> postService.create(post, blockedUser));
    }

    @Test
    public void testAddLike(){
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();

        when(postService.addLike(post, user)).thenReturn(post);

        Post likedPost = postService.addLike(post, user);

        assertEquals(post, likedPost);
        verify(postRepository).addLike(post, user);
    }

    @Test
    public void testGetTopCommentedPosts(){
        Post post1 = Helpers.createMockPost();
        Post post2 = Helpers.createMockPost();

        List<Post> expectedPosts = List.of(post1, post2);

        when(postRepository.getTopCommentedPosts()).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.getTopCommentedPosts();

        assertEquals(expectedPosts, actualPosts);
        verify(postRepository).getTopCommentedPosts();
    }

    @Test
    public void testGetMostRecentPosts(){
        Post post1 = Helpers.createMockPost();
        Post post2 = Helpers.createMockPost();
        List<Post> expectedPosts = List.of(post1, post2);

        when(postRepository.getRecentPosts()).thenReturn(expectedPosts);

        List<Post> actualPosts = postService.getRecentPosts();

        assertEquals(expectedPosts, actualPosts);
        verify(postRepository).getRecentPosts();
    }

    @Test
    void create_ValidPost_Success() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();
        post.setCreatedBy(user);

        when(postRepository.getByTitle(post.getTitle())).thenThrow(EntityNotFoundException.class);

        assertDoesNotThrow(() -> postService.create(post, user));
        verify(postRepository).create(post);
    }

    @Test
    void create_DuplicateTitle_ThrowsEntityDuplicateException() {
        User user = Helpers.createMockUser();
        Post post = Helpers.createMockPost();

        when(postRepository.getByTitle(post.getTitle())).thenReturn(post);

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
