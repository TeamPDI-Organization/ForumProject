package com.example.forumproject.controllers.MVC;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
import com.example.forumproject.models.UserFilterOptionsDto;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/admin")
    public String getAllUsers(@ModelAttribute("userFilterOptions")UserFilterOptionsDto userFilterOptionsDto,
                              Model model,
                              HttpSession session) {

        UserFilterOptions userFilterOptions = new UserFilterOptions(
                userFilterOptionsDto.getUsername(),
                userFilterOptionsDto.getEmail(),
                userFilterOptionsDto.getFirstName());

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            if (user.isAdmin()) {
                model.addAttribute("userFilterOptions", userFilterOptionsDto);
                List<User> users = userService.searchUsers(userFilterOptions);
                model.addAttribute("users", users);
                return "AdminView";
            }
            model.addAttribute("statusCode", HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return "error-view";
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

    }
}
