package com.example.forumproject.controllers.MVC;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
import com.example.forumproject.services.PostService;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
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

    private final AuthenticationHelper authenticationHelper;

    public HomeMvcController(UserService userService, PostService postService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.postService = postService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showHomePage(Model model, PostFilterOptions filterOptions, HttpSession session) {
        model.addAttribute("userCount", userService.getUsers().size());
        model.addAttribute("postCount", postService.getPosts(filterOptions).size());
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
        } catch (AuthorizationException e) {
            return "HomeView";
        }

        return "HomeView";
    }

    @GetMapping("/about")
    public String about(Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", currentUser);
        } catch (AuthorizationException e) {
            return "AboutView";
        }
        return "AboutView";
    }

}
