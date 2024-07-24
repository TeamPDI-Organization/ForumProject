package com.example.forumproject.services;

import com.example.forumproject.models.FilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> getPosts(FilterOptions filterOptions);

    List<Post> getByUserId(int userId);

    Post getPostById(int postId);

    Post getByTitle(String title);

    void create(Post post, User user);

    void update(Post post, User user);

    void delete(int id, User user);

    Post addLike(Post post, User user);
    List<Post> getTopCommentedPosts();
    List<Post> getRecentPosts();
}
