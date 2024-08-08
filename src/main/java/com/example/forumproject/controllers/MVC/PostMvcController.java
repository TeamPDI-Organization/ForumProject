package com.example.forumproject.controllers.MVC;

import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.PostMapper;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostMvcController {

    private final PostService postService;

    private final PostMapper postMapper;

    private final AuthenticationHelper authenticationHelper;

    public PostMvcController(PostService postService,
                             PostMapper postMapper,
                             AuthenticationHelper authenticationHelper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping()
    public String showAllPosts(PostFilterOptions filterOptions, Model model) {
        List<Post> posts = postService.getPosts(filterOptions);
        model.addAttribute("posts", posts);

        return "PostsView";
    }


}
