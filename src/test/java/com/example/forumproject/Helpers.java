package com.example.forumproject;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.time.LocalDateTime;

public class Helpers {

    public static User createMockUser(){

        User mockUser = new User();
        mockUser.setId(100);
        mockUser.setUsername("mockUsername");
        mockUser.setPassword("mockPassword");
        mockUser.setEmail("mockUser@email.com");
        mockUser.setFirstName("mockFirstName");
        mockUser.setLastName("mockLastName");

        return mockUser;
    }

    public static Post createMockPost(){

        Post mockPost = new Post();
        mockPost.setId(100);
        mockPost.setTitle("Mock Post Random Title");
        mockPost.setCreatedBy(createMockUser());
        mockPost.setContent("Some content which is needed in order to test the mock Post.");
        mockPost.setCreationDate(LocalDateTime.now());
        return mockPost;
    }
}
