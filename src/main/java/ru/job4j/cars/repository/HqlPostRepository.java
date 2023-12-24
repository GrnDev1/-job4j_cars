package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class HqlPostRepository implements PostRepository {
    private final CrudRepository crudRepository;

    @Override
    public AutoPost save(AutoPost post) {
        crudRepository.run(session -> {
            session.persist(post);
        });
        return post;
    }

    @Override
    public boolean update(AutoPost post) {
        try {
            crudRepository.run(session -> {
                session.update(post);
            });
            return true;
        } catch (Exception e) {
            log.error("AutoPost with this id is not found", e);
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE AutoPost WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }

    @Override
    public List<AutoPost> findAll() {
        return crudRepository.query("FROM AutoPost ORDER BY id", AutoPost.class);
    }

    @Override
    public Optional<AutoPost> findById(int id) {
        return crudRepository.optional(
                "from AutoPost WHERE id = :fId", AutoPost.class,
                Map.of("fId", id)
        );
    }
}
