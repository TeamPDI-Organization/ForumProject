package com.example.forumproject.helpers;

import com.example.forumproject.models.Comment;
import com.example.forumproject.models.CommentDto;
import com.example.forumproject.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateCommentMapper {
    private final CommentService commentService;
    @Autowired
    public UpdateCommentMapper(CommentService commentService) {
        this.commentService = commentService;
    }

    public Comment fromDto(int id, CommentDto commentDto) {
        Comment comment = commentService.getCommentById(id);
        comment.setContent(commentDto.getContent());
        return comment;
    }
}
