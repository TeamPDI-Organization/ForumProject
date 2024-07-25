package com.example.forumproject.services;

import com.example.forumproject.exceptions.AuthorizationException;
import com.example.forumproject.exceptions.EntityDuplicateException;
import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import com.example.forumproject.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private static final String MODIFY_POST_ERROR_MESSAGE = "Only admin/moderator or post creator can modify a post.";
    private static final String BLOCKED_USER_ERROR_MESSAGE = "User is blocked and cannot perform this action.";


    private final PostRepository postRepository;
    private final UserService userService;


    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public List<Post> getPosts(PostFilterOptions postFilterOptions) {
        return postRepository.getPosts(postFilterOptions);
    }

    @Override
    public List<Post> getByUserId(int id) {
        return postRepository.getByUserId(id);
    }

    @Override
    public Post getPostById(int postId) {
        return postRepository.getPostById(postId);
    }

    @Override
    public Post getByTitle(String title) {
        return postRepository.getByTitle(title);
    }

    @Override
    public void create(Post post, User user) {
        checkUserBlocked(user);

        boolean duplicateExists = true;

        try {
            postRepository.getByTitle(post.getTitle());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        post.setCreatedBy(user);
        postRepository.create(post);
    }

    @Override
    public void update(Post post, User user) {
        checkUserBlocked(user);
        checkModifyPermissions(post.getId(), user);

        boolean duplicateExists = true;
        try {
            Post existingPost = postRepository.getByTitle(post.getTitle());
            if (existingPost.getId() == post.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Post", "title", post.getTitle());
        }

        postRepository.update(post);
    }

    @Override
    public void delete(int id, User user) {
        checkUserBlocked(user);
        checkModifyPermissions(id, user);
        postRepository.delete(id);
    }

    @Override
    public Post addLike(Post post, User user) {
        return postRepository.addLike(post, user);
    }

    @Override
    public List<Post> getTopCommentedPosts() {
        return postRepository.getTopCommentedPosts();
    }

    @Override
    public List<Post> getRecentPosts() {
        return postRepository.getRecentPosts();
    }

    public void checkModifyPermissions(int postId, User user) {
        Post post = postRepository.getPostById(postId);
        if (!(user.isAdmin() || user.isModerator() || post.getCreatedBy().equals(user))) {
            throw new AuthorizationException(MODIFY_POST_ERROR_MESSAGE);
        }
    }

    private void checkUserBlocked(User user) {
        if (user.isBlocked()) {
            throw new AuthorizationException(BLOCKED_USER_ERROR_MESSAGE);
        }
    }
}
