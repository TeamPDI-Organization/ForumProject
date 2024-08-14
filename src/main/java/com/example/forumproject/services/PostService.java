package com.example.forumproject.services;

import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostService {

    List<Post> getPosts(PostFilterOptions postFilterOptions);

    List<Post> getByUserId(int userId);

    Post getPostById(int postId);

    Post getByTitle(String title);

    Post create(Post post, User user);

    Post update(Post post, User user);

    void delete(int id, User user);

    void like(Post post, User user);

    List<Post> getTopCommentedPosts();

    List<Post> getRecentPosts();
}
