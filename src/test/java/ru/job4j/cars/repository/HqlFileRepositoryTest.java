package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.File;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlFileRepositoryTest {
    private static CrudRepository crudRepository;
    private static HqlFileRepository fileRepository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        fileRepository = new HqlFileRepository(crudRepository);
    }

    @AfterEach
    public void clearFiles() {
        var files = fileRepository.findAll();
        for (File file : files) {
            fileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var file = new File();
        file.setName("file");
        file.setPath("path");
        assertThat(fileRepository.save(file)).isEqualTo(file);
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var file = new File();
        file.setName("file");
        file.setPath("path");
        fileRepository.save(file);
        var result = fileRepository.findById(file.getId()).get();
        assertThat(result).isEqualTo(file);
    }

    @Test
    public void whenFindAllThenGetSame() {
        var file = new File();
        file.setName("file");
        file.setPath("path");
        fileRepository.save(file);
        var result = fileRepository.findAll();
        assertThat(result).isEqualTo(List.of(file));
    }

    @Test
    public void whnFindAllThenGetNothing() {
        var result = fileRepository.findAll();
        assertThat(result).isEqualTo(List.of());
    }
}