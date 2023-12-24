package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
@Slf4j
public class HqlEngineRepository implements EngineRepository {
    private final CrudRepository crudRepository;

    @Override
    public List<Engine> findAll() {
        return crudRepository.query("FROM Engine ORDER BY id", Engine.class);
    }

    @Override
    public Optional<Engine> findById(int id) {
        return crudRepository.optional(
                "from Engine WHERE id = :fId", Engine.class,
                Map.of("fId", id)
        );
    }
}