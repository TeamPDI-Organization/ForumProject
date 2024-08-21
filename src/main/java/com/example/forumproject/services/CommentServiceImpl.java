package com.example.forumproject.services;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Override
    public List<Comment> getAllComments(int postId) {
        return commentRepository.getById(postId);
    }

    @Override
    public Comment createComment(Comment comment) {

        commentRepository.createComment(comment);

        return comment;
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }

    @Override
    public Comment update(Comment comment, User user) {
        return commentRepository.update(comment);
    }

    @Override
    public void deleteComment(Comment comment, User user){

        commentRepository.delete(comment);
    }

    private boolean checkIfUpdaterIsSameAsCreator(User user, Comment comment) {
        return comment.getCreatedBy().equals(user);
    }

    public boolean checkIfUserIsAdminOrModerator(User user){
        if (!(user.isAdmin() || user.isModerator())) {
            return false;
        }
        return true;
    }
}