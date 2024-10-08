package com.example.forumproject.helpers;

import com.example.forumproject.models.*;
import com.example.forumproject.repositories.PostRepository;
import com.example.forumproject.services.CommentService;
import com.example.forumproject.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private final CommentService commentService;
    private final PostService postService;
    @Autowired
    public CommentMapper(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    public Comment fromDto(CommentDto commentDto, User user, Post post) {
        Comment comment = new Comment();
        comment.setContent(commentDto.getContent());
        comment.setCreatedBy(user);
        comment.setPost(post);

        return comment;
    }

    public CommentDto toDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());

        return commentDto;
    }
}
