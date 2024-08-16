package com.example.forumproject.controllers.MVC;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.CommentMapper;
import com.example.forumproject.helpers.PostMapper;
import com.example.forumproject.models.User;
import com.example.forumproject.services.CommentService;
import com.example.forumproject.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comments")
public class CommentMvcController {

    private final CommentService commentService;

    private final CommentMapper commentMapper;

    private final AuthenticationHelper authenticationHelper;

    private final PostService postService;

    private final PostMapper postMapper;


    public CommentMvcController(CommentService commentService, CommentMapper commentMapper, AuthenticationHelper authenticationHelper, PostService postService, PostMapper postMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @PostMapping("/{id}/delete")
    public String deleteComment(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            commentService.deleteComment(commentService.getCommentById(id), user);
            return "redirect:/";
        } catch (AuthorizationException e) {
            return "unauthorized-user";
        }
    }

    @GetMapping("/{id}/update")
    public String showUpdateComment(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            commentService.getCommentById(id);
            return "comment-update"; // Return the view name directly
        } catch (AuthorizationException e) {
            return "unauthorized-user";
        }
    }

    @PostMapping("/{id}/update")
    public String updateComment(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            commentService.update(commentService.getCommentById(id), user);
            return "redirect:/";
        } catch (AuthorizationException e) {
            return "unauthorized-user";
        }
    }
}