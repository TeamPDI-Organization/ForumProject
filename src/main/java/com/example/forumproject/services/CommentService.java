package com.example.forumproject.services;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.Post;

public interface CommentService {
    Comment getById(int id);
}
