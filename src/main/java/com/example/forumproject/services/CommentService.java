package com.example.forumproject.services;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;

import java.util.List;
public interface CommentService {
    List<Comment> getAllComments(int postId);

    Comment createComment(Comment comment);

    Comment getCommentById(int id);

    Comment update(Comment comment, User user);
}
