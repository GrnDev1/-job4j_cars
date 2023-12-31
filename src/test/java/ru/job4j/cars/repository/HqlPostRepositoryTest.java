package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlPostRepositoryTest {
    private static CrudRepository crudRepository;
    private static HqlPostRepository postRepository;
    private static HqlUserRepository userRepository;
    private static HqlCarRepository carRepository;
    private static BrandRepository brandRepository;
    private static EngineRepository engineRepository;
    private static FileRepository fileRepository;
    private static Car car;
    private static User user;
    private static Brand brand;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

        crudRepository = new CrudRepository(sf);
        userRepository = new HqlUserRepository(crudRepository);
        postRepository = new HqlPostRepository(crudRepository);
        carRepository = new HqlCarRepository(crudRepository);
        brandRepository = new HqlBrandRepository(crudRepository);
        engineRepository = new HqlEngineRepository(crudRepository);
        fileRepository = new HqlFileRepository(crudRepository);

        user = new User();
        user.setLogin("login");
        user.setPassword("password");
        userRepository.create(user);

        var engine = new Engine();
        engine.setName("engine1");
        engineRepository.save(engine);

        brand = new Brand();
        brand.setName("brand1");
        brandRepository.save(brand);

        car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        carRepository.save(car);
    }

    @AfterEach
    public void clearPosts() {
        var files = fileRepository.findAll();
        for (File file : files) {
            fileRepository.deleteById(file.getId());
        }

        var posts = postRepository.findAll();
        for (AutoPost post : posts) {
            postRepository.deleteById(post.getId());
        }
    }

    @AfterAll
    public static void clearOtherTables() {
        var cars = carRepository.findAll();
        for (Car car : cars) {
            carRepository.deleteById(car.getId());
        }

        var brands = brandRepository.findAll();
        for (Brand brand : brands) {
            brandRepository.deleteById(brand.getId());
        }

        var engines = engineRepository.findAll();
        for (Engine engine : engines) {
            engineRepository.deleteById(engine.getId());
        }

        var users = userRepository.findAll();
        for (User user : users) {
            userRepository.delete(user.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        assertThat(postRepository.save(post)).isEqualTo(post);
    }

    @Test
    public void whenUpdateThenGetTrue() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        postRepository.save(post);

        var post2 = new AutoPost();
        post2.setId(post.getId());
        post2.setDescription("abcd");
        post2.setUser(user);
        post2.setCar(car);
        assertThat(postRepository.update(post2)).isTrue();
        assertThat(postRepository.findById(post2.getId()).get()).isEqualTo(post2);
    }

    @Test
    public void whenUpdateThenGetFalse() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        assertThat(postRepository.update(post)).isFalse();
    }

    @Test
    public void whenDeleteThenGetTrue() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        postRepository.save(post);
        assertThat(postRepository.deleteById(post.getId())).isTrue();
    }

    @Test
    public void whenDeleteThenGetFalse() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        assertThat(postRepository.deleteById(post.getId())).isFalse();
    }

    @Test
    public void whenFindAllThenGetSame() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        List<AutoPost> expected = List.of(post);
        postRepository.save(post);
        assertThat(postRepository.findAll()).isEqualTo(expected);
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        postRepository.save(post);
        assertThat(postRepository.findById(post.getId()).get()).isEqualTo(post);
    }

    @Test
    public void whenFindByIdThenGetNothing() {
        var post = new AutoPost();
        post.setDescription("abc");
        post.setUser(user);
        post.setCar(car);
        assertThat(postRepository.findById(post.getId())).isEmpty();
    }

    @Test
    public void whenFindAllForLastDayThenGetSame() {
        var post1 = new AutoPost();
        post1.setDescription("abc");
        post1.setUser(user);
        post1.setCar(car);
        post1.setCreated(LocalDateTime.now().minusHours(10));

        var post2 = new AutoPost();
        post2.setDescription("abc");
        post2.setUser(user);
        post2.setCar(car);
        post2.setCreated(LocalDateTime.now().minusHours(30));

        List<AutoPost> expected = List.of(post1);
        postRepository.save(post1);
        postRepository.save(post2);
        assertThat(postRepository.findAllForLastDay()).isEqualTo(expected);
    }

    @Test
    public void whenFindAllWithPhotoThenGetSame() {
        var post1 = new AutoPost();
        post1.setDescription("abc");
        post1.setUser(user);
        post1.setCar(car);

        var file = new File();
        file.setName("file");
        file.setPath("path");
        post1.setPhotos(Set.of(file));

        var post2 = new AutoPost();
        post2.setDescription("abc");
        post2.setUser(user);
        post2.setCar(car);

        List<AutoPost> expected = List.of(post1);
        postRepository.save(post1);
        postRepository.save(post2);
        assertThat(postRepository.findAllWithPhoto()).isEqualTo(expected);
    }

    @Test
    public void whenFindAllByBrandPhotoThenGetSame() {
        var post1 = new AutoPost();
        post1.setDescription("abc");
        post1.setUser(user);
        post1.setCar(car);

        List<AutoPost> expected = List.of(post1);
        postRepository.save(post1);
        assertThat(postRepository.findAllByBrand(brand)).isEqualTo(expected);
    }

    @Test
    public void whenFindAllByBrandPhotoThenGetEmpty() {
        var post1 = new AutoPost();
        post1.setDescription("abc");
        post1.setUser(user);
        post1.setCar(car);

        var brand2 = new Brand();
        brand2.setName("brand2");
        brandRepository.save(brand2);

        postRepository.save(post1);
        assertThat(postRepository.findAllByBrand(brand2)).isEqualTo(List.of());
    }
}