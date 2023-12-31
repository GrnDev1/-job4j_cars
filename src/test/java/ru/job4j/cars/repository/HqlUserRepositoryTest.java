package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlUserRepositoryTest {

    private static CrudRepository crudRepository;
    private static HqlUserRepository userRepository;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        crudRepository = new CrudRepository(sf);
        userRepository = new HqlUserRepository(crudRepository);
    }

    @AfterEach
    public void clearUsers() {
        var users = userRepository.findAll();
        for (User user : users) {
            userRepository.delete(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        assertThat(userRepository.create(user).get()).isEqualTo(user);
    }

    @Test
    public void whenSaveThenGetEmpty() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        var user2 = new User();
        user2.setLogin("login");
        user2.setPassword("password");
        assertThat(userRepository.create(user2)).isEmpty();
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        var result = userRepository.findById(user.getId()).get();
        assertThat(user).isEqualTo(result);
    }

    @Test
    public void whenFindAllThenGetSame() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        var result = userRepository.findAll();
        assertThat(result).isEqualTo(List.of(user));
    }

    @Test
    public void whenFindAllThenGetNothing() {
        var result = userRepository.findAll();
        assertThat(result).isEqualTo(List.of());
    }

    @Test
    public void whenUpdateThenGetSame() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);

        var user2 = new User();
        user2.setId(user.getId());
        user2.setLogin("login");
        user2.setPassword("password");
        assertThat(userRepository.update(user2)).isTrue();
        assertThat(userRepository.findById(user2.getId()).get()).isEqualTo(user2);
    }

    @Test
    public void whenUpdateThenGetEmpty() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        assertThat(userRepository.update(user)).isFalse();
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void whenDeleteThenGetTrue() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);
        assertThat(userRepository.delete(user.getId())).isTrue();
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void whenDeleteThenGetFalse() {
        var user = new User();
        user.setLogin("login");
        user.setPassword("password");
        assertThat(userRepository.delete(user.getId())).isFalse();
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    public void whenFindByLikeLoginThenGetSame() {
        var user1 = new User();
        user1.setLogin("login");
        user1.setPassword("password");
        var user2 = new User();
        user2.setLogin("log");
        user2.setPassword("password");
        List<User> expected = List.of(user1, user2);
        userRepository.create(user1);
        userRepository.create(user2);
        assertThat(userRepository.findByLikeLogin("log")).isEqualTo(expected);
    }

    @Test
    public void whenFindByLoginThenGetSame() {
        var user1 = new User();
        user1.setLogin("login");
        user1.setPassword("password");
        userRepository.create(user1);
        assertThat(userRepository.findByLogin("login").get()).isEqualTo(user1);
    }

}