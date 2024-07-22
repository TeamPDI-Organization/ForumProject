package com.example.forumproject.repositories;

import com.example.forumproject.models.Comment;

import java.util.List;

public interface CommentRepository {

    List<Comment> getAll();

    void create(Comment comment);

    void update(Comment comment);

    void delete(int id);
}
