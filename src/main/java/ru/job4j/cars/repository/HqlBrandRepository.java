package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class HqlBrandRepository implements BrandRepository {
    private final CrudRepository crudRepository;

    @Override
    public Brand save(Brand brand) {
        crudRepository.run(session -> session.persist(brand));
        return brand;
    }

    @Override
    public List<Brand> findAll() {
        return crudRepository.query("FROM Brand ORDER BY id", Brand.class);
    }

    @Override
    public Optional<Brand> findById(int id) {
        return crudRepository.optional(
                "from Brand WHERE id = :fId", Brand.class,
                Map.of("fId", id)
        );
    }

    @Override
    public boolean deleteById(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE Brand WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }
}