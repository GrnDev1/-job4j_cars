package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.File;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    @Override
    public List<AutoPost> findAllForLastDay() {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        return crudRepository.query(
                "from AutoPost WHERE created > :time", AutoPost.class,
                Map.of("time", time)
        );
    }

    @Override
    public List<AutoPost> findAllWithPhoto() {
        Function<Session, List<AutoPost>> command = session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AutoPost> criteria = builder.createQuery(AutoPost.class);
            Root<AutoPost> postRoot = criteria.from(AutoPost.class);
            Join<AutoPost, File> fileJoin = postRoot.join("file");
            criteria.select(postRoot).where(builder.isNotNull(fileJoin.get("file")));
            return session.createQuery(criteria).getResultList();
        };
        return crudRepository.tx(command);
    }

    @Override
    public List<AutoPost> findAllByBrand(Brand brand) {
        Function<Session, List<AutoPost>> command = session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<AutoPost> criteria = builder.createQuery(AutoPost.class);
            Root<AutoPost> postRoot = criteria.from(AutoPost.class);
            Join<AutoPost, Car> carJoin = postRoot.join("car");
            criteria.select(postRoot).where(builder.equal(carJoin.get("car").get("brand"), brand));
            return session.createQuery(criteria).getResultList();
        };
        return crudRepository.tx(command);
    }
}
