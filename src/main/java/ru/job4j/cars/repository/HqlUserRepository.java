package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
@Slf4j
public class HqlUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> create(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (Exception e) {
            log.error("User with this mail already exists", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(User user) {
        try {
            crudRepository.run(session -> session.update(user));
            return true;
        } catch (Exception e) {
            log.error("User with this id is not found", e);
        }
        return false;
    }

    @Override
    public boolean delete(int userId) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE User WHERE id = :fId")
                        .setParameter("fId", userId)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }

    @Override
    public List<User> findAll() {
        return crudRepository.query("from User order by id asc", User.class);
    }

    @Override
    public Optional<User> findById(int userId) {
        return crudRepository.optional(
                "from User where id = :fId", User.class,
                Map.of("fId", userId)
        );
    }

    @Override

    public List<User> findByLikeLogin(String key) {
        return crudRepository.query(
                "from User where login like :fKey", User.class,
                Map.of("fKey", "%" + key + "%")
        );
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return crudRepository.optional(
                "from User where login = :fLogin", User.class,
                Map.of("fLogin", login)
        );
    }
}