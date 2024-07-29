package com.example.forumproject.repositories;

import com.example.forumproject.exceptions.EntityNotFoundException;
import com.example.forumproject.models.PhoneNumber;
import com.example.forumproject.models.User;
import com.example.forumproject.models.UserFilterOptions;
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
public class UserRepositoryImpl implements UserRepository{

    private SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query.setParameter("username", username);

            List<User> result = query.list();
            if (result.size() == 0) {
                throw new EntityNotFoundException("Post", "name", username);
            }

            return result.get(0);
        }
    }

    @Override
    public User create(User user) {
            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();
                session.persist(user);
                session.getTransaction().commit();
            }
            return user;
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            user.setActive(false);
            session.getTransaction().commit();
        }
    }

    @Override
    public User update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
        return user;
    }

    @Override
    public List<User> searchUsers(UserFilterOptions options) {
        try (Session session = sessionFactory.openSession()){
            StringBuilder queryBuilder = new StringBuilder("From User");
            ArrayList<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            options.getUsername().ifPresent(value ->{
                filters.add(" username like :username ");
                params.put("username", String.format("%%%s%%", value));
            });

            options.getEmail().ifPresent(value ->{
                filters.add(" email like :email ");
                params.put("email", String.format("%%%s%%", value));
            });

            options.getFirstName().ifPresent(value -> {
                filters.add(" firstName like :firstName ");
                params.put("firstName", String.format("%%%s%%", value));
            });

            if (!filters.isEmpty()) {
                queryBuilder.append(" WHERE ")
                        .append(String.join("AND", filters));
            }
            Query<User> query = session.createQuery(queryBuilder.toString(), User.class);
            query.setProperties(params);

            return query.list();
        }
    }

    @Override
    public PhoneNumber setPhoneNumber(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(phoneNumber);
            session.getTransaction().commit();

            return phoneNumber;
        }
    }

    @Override
    public PhoneNumber getPhoneNumber(int userId) {
        try (Session session = sessionFactory.openSession()) {
            PhoneNumber phoneNumber = session.get(PhoneNumber.class, userId);

            return phoneNumber;
        }
    }

    @Override
    public User makeModerator(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.get(User.class, userId);
            if (user == null) {
                throw new EntityNotFoundException("User", userId);
            }
            user.setModerator(true);
            session.getTransaction().commit();

            return user;
        }
    }
}
