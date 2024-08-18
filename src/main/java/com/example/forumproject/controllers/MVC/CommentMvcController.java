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

    @PostMapping ("/{id}/delete")
    public String deleteComment(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            commentService.deleteComment(commentService.getCommentById(id), user);
            return "redirect:/posts/%d".formatted(commentService.getCommentById(id).getPost().getId());
        } catch (AuthorizationException e) {
            return "error-view";
        }
    }

    @GetMapping("/{id}/update")
    public String showUpdateComment(@PathVariable int id, Model model, HttpSession session) {


        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Comment comment = commentService.getCommentById(id);
            CommentDto commentDto = commentMapper.toDto(comment);
            model.addAttribute("comment", commentDto);
            return "comment-update";
        } catch (AuthorizationException e) {
            return "error-view";
        }
    }

    @PostMapping("/{id}/update")
    public String updateComment(@PathVariable int id, @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult errors,
                                HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "comment-update";
        }

        try {
            Comment existingComment = commentService.getCommentById(id);
            Comment comment = commentMapper.fromDto(commentDto, user, existingComment.getPost());
            comment.setId(id);
            commentService.update(comment, user);
            return "redirect:/posts/%d".formatted(comment.getPost().getId());
        } catch (AuthorizationException e) {
            return "error-view";
        }
    }

    @GetMapping("/{id}/check-creator")
    @ResponseBody
    public boolean checkCommentCreator(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return false;
        }

        try {
            Comment comment = commentService.getCommentById(id);
            return comment.getCreatedBy().equals(user);
        } catch (AuthorizationException e) {
            return false;
        }
    }

    @GetMapping("/current-user-id")
    @ResponseBody
    public Integer getCurrentUserId(HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            return user.getId();
        } catch (AuthorizationException e) {
            return null;
        }
    }

    @GetMapping("/current-user-roles")
    @ResponseBody
    public Map<String, Boolean> getCurrentUserRoles(HttpSession session) {
        Map<String, Boolean> roles = new HashMap<>();
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            roles.put("isAdmin", user.isAdmin());
            roles.put("isModerator", user.isModerator());
        } catch (AuthorizationException e) {
            roles.put("isAdmin", false);
            roles.put("isModerator", false);
        }
        return roles;
    }
}