package com.example.forumproject.controllers.MVC;

import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    private final UserService userService;

    private final PostService postService;

    public HomeMvcController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model, PostFilterOptions filterOptions) {
        model.addAttribute("userCount", userService.getUsers().size());
        model.addAttribute("postCount", postService.getPosts(filterOptions).size());

        return "HomeView";
    }

    @GetMapping("/about")
    public String about() {
        return "AboutView";
    }
}
