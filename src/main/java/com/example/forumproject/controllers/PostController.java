package com.example.forumproject.controllers;

import com.example.forumproject.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    public PostController() {
    }

    @GetMapping
    public List<Post> getPosts() {
        return null;
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable int id) {
        return null;
    }


}
