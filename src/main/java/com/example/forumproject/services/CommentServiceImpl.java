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
    private CommentRepository commentRepository;
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
        return commentRepository.createComment(comment);
    }

    @Override
    public Comment getCommentById(int id) {
        return commentRepository.getCommentById(id);
    }

    @Override
    public Comment update(Comment comment, User user) {
        checkIfUpdaterIsSameAsCreator(user, comment);
        return commentRepository.update(comment);
    }

    @Override
    public Comment deleteComment(Comment comment, User user){
        if (!checkIfUpdaterIsSameAsCreator(user, comment)){
            throw new AuthorizationException("You do not have permission to delete this post");
        }
        if (!checkIfUserIsAdminOrModerator(user)){
            throw new AuthorizationException("You do not have permission to delete this post");
        }
        return commentRepository.delete(comment);
    }

    private boolean checkIfUpdaterIsSameAsCreator(User user, Comment comment) {
        if (user.getId() != comment.getId()){
            return false;
        }
        return true;
    }

    private boolean checkIfUserIsAdminOrModerator(User user){
        if (user.isAdmin()){
            return true;
        }
        if (user.isModerator()){
            return true;
        }
        return false;
    }
}