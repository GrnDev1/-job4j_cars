package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class HqlCarRepository implements CarRepository {
    private final CrudRepository crudRepository;

    @Override
    public Car save(Car car) {
        crudRepository.run(session -> session.persist(car));
        return car;
    }

    @Override
    public boolean update(Car car) {
        try {
            crudRepository.run(session -> session.update(car));
            return true;
        } catch (Exception e) {
            log.error("Car with this id is not found", e);
        }
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE Car WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }

    @Override
    public List<Car> findAll() {
        return crudRepository.query("FROM Car ORDER BY id", Car.class);
    }

    @Override
    public Optional<Car> findById(int id) {
        return crudRepository.optional(
                "from Car WHERE id = :fId", Car.class,
                Map.of("fId", id)
        );
    }
}
