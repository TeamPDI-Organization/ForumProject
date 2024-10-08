package com.example.forumproject.controllers.MVC;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.CommentMapper;
import com.example.forumproject.helpers.PostMapper;
import com.example.forumproject.models.*;
import com.example.forumproject.services.CommentService;
import com.example.forumproject.services.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;

    private final CommentService commentService;

    private final PostMapper postMapper;

    private final CommentMapper commentMapper;

    private final AuthenticationHelper authenticationHelper;

    public PostMvcController(PostService postService, CommentService commentService,
                             PostMapper postMapper, CommentMapper commentMapper,
                             AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.commentService = commentService;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{id}")
    public String showPost(@PathVariable int id, Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", currentUser);
            Post post = postService.getPostById(id);
            model.addAttribute("post", post);
            model.addAttribute("comment", new CommentDto());

            return "SinglePostView";
        } catch (EntityNotFoundException e) {
            return "not-found";
        }
    }

    @GetMapping()
    public String showAllPosts(@ModelAttribute("postFilterOptions") PostFilterOptionsDto filterOptionsDto,
                               Model model, HttpSession session) {
        PostFilterOptions filterOptions = new PostFilterOptions(
                filterOptionsDto.getTitle(),
                filterOptionsDto.getContent(),
                filterOptionsDto.getSortBy(),
                filterOptionsDto.getSortOrder());

        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", currentUser);
        } catch (AuthorizationException e) {
            List<Post> post = postService.getRecentPosts();
            model.addAttribute("posts", post);

            return "PostsView";
        }

        model.addAttribute("postFilterOptions", filterOptionsDto);
        List<Post> posts = postService.getPosts(filterOptions);
        model.addAttribute("posts", posts);

        return "PostsView";
    }

    @GetMapping("/{id}/comments")
    public String getCommentsForPost(@PathVariable int id, Model model) {
        List<Comment> comments = commentService.getAllComments(id);
        model.addAttribute("comments", comments);
        return "SinglePostView : comments-feed";
    }

    @GetMapping("/new")
    public String showNewPost(Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", currentUser);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        model.addAttribute("post", new PostDto());

        return "post-new";
    }

    @PostMapping("/new")
    public String createPost(@Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult errors,
                             HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "post-new";
        }

        try {
            Post post = postMapper.fromDto(postDto);
            postService.create(post, user);

            return "redirect:/posts/%d".formatted(post.getId());
        } catch (EntityDuplicateException e) {
            errors.rejectValue("title", "duplicate", "Post with this title already exists");
            return "post-new";
        }
    }

    @GetMapping("/{id}/update")
    public String showEditPostPage(@PathVariable int id,
                                   Model model,
                                   HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getPostById(id);
            PostDto postDto = postMapper.toDto(post);
            model.addAttribute("post", postDto);
            return "post-update";
        } catch (EntityNotFoundException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute("post") PostDto postDto,
                             BindingResult errors,
                             HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "post-update";
        }

        try {
            Post post = postMapper.fromDto(id, postDto);
            postService.update(post, user);

            return "redirect:/posts/%d".formatted(id);
        } catch (EntityDuplicateException e) {
            errors.rejectValue("title", "duplicate", "Post with this title already exists");
            return "post-update";
        } catch (AuthorizationException e) {
            return "error-view";
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            postService.delete(id, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            return "not-found";
        } catch (AuthorizationException e) {
            return "error-view";
        }
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable int id, HttpSession session) {
        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            Post post = postService.getPostById(id);
            postService.like(post, user);
            return "redirect:/posts";
        } catch (EntityNotFoundException e) {
            return "not-found";
        }
    }

    @GetMapping("/{id}/comment")
    public String showCommentForm(@PathVariable int id, Model model) {
        model.addAttribute("comment", new CommentDto());
        return "redirect:/posts/%d".formatted(id);
    }

    @PostMapping("/{id}/comment")
    public String createComment(@PathVariable int id, @Valid @ModelAttribute("comment") CommentDto commentDto,
                                BindingResult errors, HttpSession session) {

        User user;
        try {
            user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "redirect:/posts/%d".formatted(id);
        }

        try {
            Post post = postService.getPostById(id);
            Comment comment = commentMapper.fromDto(commentDto, user, post);
            commentService.createComment(comment);
            post.getComments().add(comment);
            return "redirect:/posts/%d".formatted(id);
        } catch (EntityNotFoundException e) {
            return "not-found";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
