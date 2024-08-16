package com.example.forumproject.repositories;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.PostFilterOptions;
import com.example.forumproject.models.Post;
import com.example.forumproject.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PostRepositoryImpl implements PostRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PostRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Post> getPosts(PostFilterOptions postFilterOptions) {
        try (Session session = sessionFactory.openSession()) {
            StringBuilder queryBuilder = new StringBuilder("From Post");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            postFilterOptions.getTitle().ifPresent(value -> {
                filters.add(" title like :title ");
                params.put("title", String.format("%%%s%%", value));
            });

            if (!filters.isEmpty()) {
                queryBuilder.append(" WHERE ")
                        .append(String.join("AND", filters));
            }

            queryBuilder.append(createOrderBy(postFilterOptions));
            Query<Post> query = session.createQuery(queryBuilder.toString(), Post.class);
            query.setProperties(params);

            return query.list();
        }
    }

    private String createOrderBy(PostFilterOptions postFilterOptions) {
        String orderBy = "";
        if (!postFilterOptions.getSortBy().isEmpty()) {
            switch (postFilterOptions.getSortBy().get()) {
                case "title":
                    orderBy = "title";
                    break;
                case "likes":
                    orderBy = "likes";
                    break;
            }
            orderBy = String.format(" order by %s", orderBy);

            if (postFilterOptions.getSortOrder().isPresent()
                    && postFilterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
                orderBy = String.format("%s DESC", orderBy);
            }
        }
        return orderBy;
    }

    @Override
    public List<Post> getByUserId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where createdBy.id = :id", Post.class);
            query.setParameter("id", id);
            List<Post> posts = query.list();
            if (posts.isEmpty()) {
                throw new EntityNotFoundException("Posts", id);
            }

            return posts;
        }
    }

    @Override
    public Post getPostById(int postId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where id = :id", Post.class);
            query.setParameter("id", postId);
            List<Post> posts = query.list();
            if (posts == null) {
                throw new EntityNotFoundException("Posts", postId);
            }
            return posts.get(0);
        }
    }

    @Override
    public Post getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Post> query = session.createQuery("from Post where title = :title", Post.class);
            query.setParameter("title", title);
            List<Post> posts = query.list();
            if (posts.isEmpty()) {
                throw new EntityNotFoundException("Post", "title", title);
            }

            return posts.get(0);
        }
    }

    @Override
    public void create(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Post post) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(post);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Post postToDelete = getPostById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(postToDelete);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Post> getTopCommentedPosts() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select p from Post p " +
                                    "left join p.comments c group by p.id order by count(c) desc", Post.class)
                    .setMaxResults(10)
                    .list();
        }
    }

    @Override
    public List<Post> getRecentPosts() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Post p order by p.creationDate desc", Post.class)
                    .setMaxResults(10)
                    .list();
        }
    }
}
