package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.File;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class HqlFileRepository implements FileRepository {
    private final CrudRepository crudRepository;

    @Override
    public File save(File file) {
        crudRepository.run(session -> {
            session.persist(file);
        });
        return file;
    }

    @Override
    public Optional<File> findById(int id) {
        return crudRepository.optional(
                "from File WHERE id = :fId", File.class,
                Map.of("fId", id)
        );
    }

    @Override
    public boolean deleteById(int id) {
        Function<Session, Boolean> command = session ->
                session.createQuery("DELETE File WHERE id = :fId")
                        .setParameter("fId", id)
                        .executeUpdate() != 0;
        return crudRepository.tx(command);
    }
}
