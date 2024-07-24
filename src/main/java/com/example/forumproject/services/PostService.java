package com.example.forumproject.services;

import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> getPosts();

    Post getById(int id);

    Post getByTitle(String title);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);

    Post addLike(Post post, User user);
}
