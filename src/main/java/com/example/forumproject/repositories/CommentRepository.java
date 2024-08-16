package com.example.forumproject.repositories;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.CommentDto;
import com.example.forumproject.models.User;

import java.util.List;

public interface CommentRepository {

    List<Comment> getById(int id);

    Comment update(Comment comment);

    void delete(Comment comment);

    Comment getCommentById(int id);

    void createComment(Comment comment);
}
