package com.example.forumproject.helpers;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.User;
import com.example.forumproject.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthenticationHelper {
    public static final String INVALID_AUTHENTICATION_ERROR = "Invalid username or password.";
    public static final String LOGGED_USER_ERROR = "No user logged in.";

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationHelper.class);

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }

        try {
            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader.startsWith("Basic ")) {
                String base64Credentials = authHeader.substring("Basic".length()).trim();
                byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);
                final String[] values = credentials.split(":", 2);

                String username = values[0];
                String password = values[1];

                User user = userService.getByUsername(username);

                logger.info("Authenticating user: {}", username);
                logger.info("User active status: {}", user.isActive());

                if (!user.isActive()) {
                    throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
                }

                if (!user.getPassword().equals(password)) {
                    throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
                }
                return user;
            } else {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
        } catch (EntityNotFoundException | ArrayIndexOutOfBoundsException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }
}
