package com.example.forumproject.repositories;

import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;

import java.util.List;

public interface PostRepository {

    List<Post> getPosts(PostFilterOptions postFilterOptions);

    List<Post> getByUserId(int userId);

    Post getPostById(int postId);

    Post getByTitle(String title);

    void create(Post post);

    void update(Post post);

    void delete(int id);

    List<Post> getTopCommentedPosts();

    List<Post> getRecentPosts();
}
