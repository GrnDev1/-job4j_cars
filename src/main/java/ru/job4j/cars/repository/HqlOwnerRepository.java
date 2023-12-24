package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class HqlOwnerRepository implements OwnerRepository {
    private final CrudRepository crudRepository;

    @Override
    public Optional<Owner> save(Owner owner) {
        try {
            crudRepository.run(session -> session.persist(owner));
            return Optional.of(owner);
        } catch (Exception e) {
            log.error("Owner with this user already exist", e);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Owner owner) {
        try {
            crudRepository.run(session -> session.update(owner));
            return true;
        } catch (Exception e) {
            log.error("Owner with this id is not found", e);
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE Owner WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }

    @Override
    public List<Owner> findAll() {
        return crudRepository.query("FROM Owner ORDER BY id", Owner.class);
    }

    @Override
    public Optional<Owner> findById(int id) {
        return crudRepository.optional(
                "from Owner WHERE id = :fId", Owner.class,
                Map.of("fId", id)
        );
    }
}