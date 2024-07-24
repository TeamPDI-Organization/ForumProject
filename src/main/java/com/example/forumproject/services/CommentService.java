package com.example.forumproject.services;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;

import java.util.List;

public interface CommentService {
    List<Comment> getAllComments(int postId);
}
