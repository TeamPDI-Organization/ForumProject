package com.example.forumproject.repositories;

import com.example.forumproject.models.FilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostRepository {

    List<Post> getPosts(FilterOptions filterOptions);

    List<Post> getByUserId(int userId);

    Post getPostById(int postId);

    Post getByTitle(String title);

    void create(Post post);

    void update(Post post);

    void delete(int id);

    Post addLike(Post post, User user);
    List<Post> getTopCommentedPosts();
    List<Post> getRecentPosts();
}
