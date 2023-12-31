package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlOwnerRepositoryTest {
    private static CrudRepository crudRepository;
    private static HqlUserRepository userRepository;
    private static HqlOwnerRepository ownerRepository;
    private static User user;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        userRepository = new HqlUserRepository(crudRepository);
        ownerRepository = new HqlOwnerRepository(crudRepository);
        user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
    }

    @AfterEach
    public void clearOwners() {
        var owners = ownerRepository.findAll();
        for (Owner owner : owners) {
            ownerRepository.deleteById(owner.getId());
        }
    }

    @AfterAll
    public static void clearUsers() {
        var users = userRepository.findAll();
        for (User user : users) {
            userRepository.delete(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        assertThat(ownerRepository.save(owner).get()).isEqualTo(owner);
    }

    @Test
    public void whenSaveThenGetEmpty() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        ownerRepository.save(owner);
        var owner2 = new Owner();
        owner2.setName("Roman");
        owner2.setUser(user);
        assertThat(ownerRepository.save(owner2)).isEmpty();
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        ownerRepository.save(owner);
        var result = ownerRepository.findById(owner.getId()).get();
        assertThat(result).isEqualTo(owner);
    }

    @Test
    public void whenFindAllThenGetSame() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        ownerRepository.save(owner);
        var result = ownerRepository.findAll();
        assertThat(result).isEqualTo(List.of(owner));
    }

    @Test
    public void whenFindAllThenGetNothing() {
        var result = ownerRepository.findAll();
        assertThat(result).isEqualTo(List.of());
    }

    @Test
    public void whenUpdateThenGetSame() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        ownerRepository.save(owner);
        var owner2 = new Owner();
        owner2.setId(owner.getId());
        owner2.setName("Roman");
        owner2.setUser(user);
        assertThat(ownerRepository.update(owner2)).isTrue();
        assertThat(ownerRepository.findById(owner2.getId()).get()).isEqualTo(owner2);
    }

    @Test
    public void whenUpdateThenGetEmpty() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        assertThat(ownerRepository.update(owner)).isFalse();
        assertThat(ownerRepository.findById(owner.getId())).isEmpty();
    }

    @Test
    public void whenDeleteThenGetTrue() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        ownerRepository.save(owner);
        assertThat(ownerRepository.deleteById(owner.getId())).isTrue();
        assertThat(ownerRepository.findById(owner.getId())).isEmpty();
    }

    @Test
    public void whenDeleteThenGetFalse() {
        var owner = new Owner();
        owner.setName("Alex");
        owner.setUser(user);
        assertThat(ownerRepository.deleteById(owner.getId())).isFalse();
        assertThat(ownerRepository.findById(owner.getId())).isEmpty();
    }
}