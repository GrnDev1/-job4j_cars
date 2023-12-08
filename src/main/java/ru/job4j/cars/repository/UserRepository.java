package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    /**
     * Сохранить в базе.
     *
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.save(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                LOG.error("User with this mail already exists", e);
            }
            return user;
        }
    }

    /**
     * Обновить в базе пользователя.
     *
     * @param user пользователь.
     */
    public void update(User user) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                session.update(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    /**
     * Удалить пользователя по id.
     *
     * @param userId ID
     */
    public void delete(int userId) {
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                User user = new User();
                user.setId(userId);
                session.delete(user);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
    }

    /**
     * Список пользователь отсортированных по id.
     *
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> result = List.of();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("FROM User ORDER BY id", User.class).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Найти пользователя по ID
     *
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        Optional<User> user = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery("FROM User WHERE id =:fId", User.class);
                query.setParameter("fId", userId);
                user = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return user;
    }

    /**
     * Список пользователей по login LIKE %key%
     *
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> result = List.of();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                result = session.createQuery("FROM User WHERE login LIKE :key", User.class).setParameter("key", "%" + key + "%").list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return result;
    }

    /**
     * Найти пользователя по login.
     *
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> user = Optional.empty();
        try (Session session = sf.openSession()) {
            try {
                session.beginTransaction();
                Query<User> query = session.createQuery("FROM User WHERE login =:fLogin", User.class);
                query.setParameter("fLogin", login);
                user = query.uniqueResultOptional();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
            }
        }
        return user;
    }
}