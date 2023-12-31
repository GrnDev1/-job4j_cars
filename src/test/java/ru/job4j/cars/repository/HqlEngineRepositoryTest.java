package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlEngineRepositoryTest {
    private static CrudRepository crudRepository;
    private static HqlEngineRepository engineRepository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        engineRepository = new HqlEngineRepository(crudRepository);
    }

    @AfterEach
    public void clearEngines() {
        var engines = engineRepository.findAll();
        for (Engine engine : engines) {
            engineRepository.deleteById(engine.getId());
        }
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var engine = new Engine();
        engine.setName("engine");
        crudRepository.run(session -> session.persist(engine));
        var result = engineRepository.findById(engine.getId()).get();
        assertThat(engine).isEqualTo(result);
    }

    @Test
    public void whenFindAllThenGetSame() {
        var engine = new Engine();
        engine.setName("engine");
        crudRepository.run(session -> session.persist(engine));
        var result = engineRepository.findAll();
        assertThat(result).isEqualTo(List.of(engine));
    }

    @Test
    public void whenFindAllThenGetNothing() {
        var result = engineRepository.findAll();
        assertThat(result).isEqualTo(List.of());
    }
}