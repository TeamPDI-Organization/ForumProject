package com.example.forumproject.controllers.MVC;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.helpers.AuthenticationHelper;
import com.example.forumproject.helpers.UpdateUserMapper;
import com.example.forumproject.models.*;
import com.example.forumproject.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    private final UpdateUserMapper updateUserMapper;

    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, UpdateUserMapper updateUserMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.updateUserMapper = updateUserMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/admin")
    public String getAllUsers(@ModelAttribute("userFilterOptions") UserFilterOptionsDto userFilterOptionsDto,
                              Model model,
                              HttpSession session) {

        UserFilterOptions userFilterOptions = new UserFilterOptions(
                userFilterOptionsDto.getUsername(),
                userFilterOptionsDto.getEmail(),
                userFilterOptionsDto.getFirstName());

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", user);
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

    @GetMapping("/{id}/view")
    public String showUserById(@PathVariable int id, Model model, HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetCurrentUser(session);
            model.addAttribute("currentUser", currentUser);

            User user = userService.getById(id);
            model.addAttribute("user", user);

            if (user.isAdmin()) {
                PhoneNumber phoneNumber = userService.getPhoneNumber(user.getId());
                model.addAttribute("phoneNumber", phoneNumber);
            }

            return "SingleUserView";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            userService.delete(id, user);
            return "redirect:/";

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/{id}/update")
    public String showUpdateUser(@PathVariable int id, Model model, HttpSession session) {


        try {
            User user = authenticationHelper.tryGetCurrentUser(session);

        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        try {
            User user = userService.getById(id);
            UpdateUserDto userDto = updateUserMapper.toDto(user);
            model.addAttribute("user", userDto);

            return "user-update";
        } catch (EntityNotFoundException e) {
            return "not-found";
        }
    }

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id, @ModelAttribute("user") UpdateUserDto userDto,
                             BindingResult errors, HttpSession session) {

        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "user-update";
        }

        try {
            User user = updateUserMapper.fromDto(id, userDto);
            userService.update(user, user);

            return "redirect:/users/%d/view".formatted(user.getId());
        } catch (AuthorizationException e) {
            return "error-view";
        }
    }

    @PostMapping("/{id}/make-moderator")
    public String makeModerator(@PathVariable int id, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            userService.makeModerator(id);
            return "redirect:/users/%d/view".formatted(id);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/remove-moderator")
    public String removeModerator(@PathVariable int id, HttpSession session) {
        try {
            authenticationHelper.tryGetCurrentUser(session);
            userService.removeModerator(id);
            return "redirect:/users/%d/view".formatted(id);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/block-user")
    public String blockUser(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            userService.blockUser(id,user);
            return "redirect:/users/%d/view".formatted(id);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/unblock-user")
    public String unblockUser(@PathVariable int id, HttpSession session) {
        try {
            User user = authenticationHelper.tryGetCurrentUser(session);
            userService.unblockUser(id,user);
            return "redirect:/users/%d/view".formatted(id);
        } catch (AuthorizationException e) {
            return "redirect:/auth/login";
        }
    }
}
