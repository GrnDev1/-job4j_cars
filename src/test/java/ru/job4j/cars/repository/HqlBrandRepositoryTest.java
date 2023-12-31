package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Brand;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlBrandRepositoryTest {

    private static CrudRepository crudRepository;
    private static HqlBrandRepository brandRepository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        brandRepository = new HqlBrandRepository(crudRepository);
    }

    @AfterEach
    public void clearBrands() {
        var brands = brandRepository.findAll();
        for (Brand brand : brands) {
            brandRepository.deleteById(brand.getId());
        }
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var brand = new Brand();
        brand.setName("brand");
        crudRepository.run(session -> session.persist(brand));
        var result = brandRepository.findById(brand.getId()).get();
        assertThat(brand).isEqualTo(result);
    }

    @Test
    public void whenFindAllThenGetSame() {
        var brand = new Brand();
        brand.setName("brand");
        crudRepository.run(session -> session.persist(brand));
        var result = brandRepository.findAll();
        assertThat(result).isEqualTo(List.of(brand));
    }

    @Test
    public void whnFindAllThenGetNothing() {
        var result = brandRepository.findAll();
        assertThat(result).isEqualTo(List.of());
    }
}