package com.example.forumproject.controllers;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.CommentMapper;
import com.example.forumproject.helpers.UpdateCommentMapper;
import com.example.forumproject.models.Comment;
import com.example.forumproject.models.CommentDto;
import com.example.forumproject.models.User;
import com.example.forumproject.services.CommentService;
import com.example.forumproject.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentMapper commentMapper;
    private final AuthenticationHelper authenticationHelper;
    private final CommentService commentService;
    private final PostService postService;
    private final UpdateCommentMapper updateCommentMapper;

    @Autowired
    public CommentController(CommentService commentService, PostService postService, AuthenticationHelper authenticationHelper, CommentMapper commentMapper, UpdateCommentMapper updateCommentMapper) {
        this.commentService = commentService;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
        this.commentMapper = commentMapper;
        this.updateCommentMapper = updateCommentMapper;
    }

    @GetMapping("/{postId}")
    public List<Comment> getComments(@PathVariable int postId) {
        try {
            return commentService.getAllComments(postId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{postId}")
    public Comment addComment(@RequestHeader HttpHeaders headers,@PathVariable int postId, @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentMapper.fromDto(commentDto, user, postId);
            return commentService.createComment(comment);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Comment updateComment(@RequestHeader HttpHeaders headers,@PathVariable int id, @RequestBody CommentDto commentDto) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = updateCommentMapper.fromDto(id, commentDto);
            return commentService.update(comment, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Comment deleteComment(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Comment comment = commentService.getCommentById(id);
            return commentService.deleteComment(comment, user);
        }catch (AuthorizationException e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
