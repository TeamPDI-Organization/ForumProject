package com.example.forumproject.services;

import com.example.forumproject.Helpers;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTests {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createComment_ValidComment_Success() {
        Comment comment = new Comment();
        comment.setContent("New comment content");

        when(commentRepository.createComment(comment)).thenReturn(comment);

        Comment createdComment = commentService.createComment(comment);
        assertNotNull(createdComment);
        assertEquals(comment.getContent(), createdComment.getContent());
    }

    @Test
    void update_ValidComment_Success() {
        User user = new User();
        Comment comment = new Comment();
        comment.setId(1);
        comment.setCreatedBy(user);

        when(commentRepository.update(comment)).thenReturn(comment);

        Comment updatedComment = commentService.update(comment, user);
        assertNotNull(updatedComment);
        assertEquals(comment.getId(), updatedComment.getId());
    }

    @Test
    void deleteComment_ValidCommentAndUserIsCreator_Success() {
        User user = Helpers.createMockUser();
        Comment comment = Helpers.createMockComment();
        comment.setCreatedBy(user);
        when(commentRepository.getCommentById(comment.getId())).thenReturn(comment);

        commentService.deleteComment(comment, user);
        verify(commentRepository).delete(comment);

    }

    @Test
    void deleteComment_ValidCommentAndUserIsAdmin_Success() {
        User creator = Helpers.createMockUser();
        creator.setId(101);
        User admin = Helpers.createMockUser();
        admin.setAdmin(true);
        Comment comment = Helpers.createMockComment();
        comment.setCreatedBy(creator);
        when(commentRepository.getCommentById(comment.getId())).thenReturn(comment);

        commentService.deleteComment(comment, admin);
        verify(commentRepository).delete(comment);

    }

    @Test
    void checkIfUserIsAdminOrModerator_AdminUser_ReturnsTrue() {
        User adminUser = new User();
        adminUser.setAdmin(true);

        assertTrue(commentService.checkIfUserIsAdminOrModerator(adminUser));
    }

    @Test
    void checkIfUserIsAdminOrModerator_NonAdminUser_ReturnsFalse() {
        User regularUser = new User();

        assertFalse(commentService.checkIfUserIsAdminOrModerator(regularUser));
    }
}
