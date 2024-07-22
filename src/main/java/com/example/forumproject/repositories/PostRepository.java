package com.example.forumproject.repositories;

import com.example.forumproject.models.Post;

import java.util.List;

public interface PostRepository {

    List<Post> getPosts();

    Post getById(int id);

    Post getByTitle(String title);

    void create(Post post);

    void update(Post post);

    void delete(int id);
}
