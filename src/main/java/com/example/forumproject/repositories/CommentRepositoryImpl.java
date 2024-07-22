package com.example.forumproject.repositories;

import com.example.forumproject.models.Comment;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final SessionFactory sessionFactory;

    public CommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Comment> getAll() {
        return List.of();
    }

    @Override
    public void create(Comment comment) {

    }

    @Override
    public void update(Comment comment) {

    }

    @Override
    public void delete(int id) {

    }
}
