package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HqlCarRepositoryTest {
    private static HqlCarRepository carRepository;
    private static HqlBrandRepository brandRepository;
    private static HqlEngineRepository engineRepository;
    private static Brand brand;
    private static Engine engine;

    @BeforeAll
    public static void initRepositories() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        CrudRepository crudRepository = new CrudRepository(sf);
        carRepository = new HqlCarRepository(crudRepository);
        brandRepository = new HqlBrandRepository(crudRepository);
        engineRepository = new HqlEngineRepository(crudRepository);

        engine = new Engine();
        engine.setName("engine1");
        engineRepository.save(engine);

        brand = new Brand();
        brand.setName("brand1");
        brandRepository.save(brand);
    }

    @AfterEach
    public void clearCars() {
        var cars = carRepository.findAll();
        for (Car car : cars) {
            carRepository.deleteById(car.getId());
        }
    }

    @AfterAll
    public static void clearTables() {
        var brands = brandRepository.findAll();
        for (Brand brand : brands) {
            brandRepository.deleteById(brand.getId());
        }

        var engines = engineRepository.findAll();
        for (Engine engine : engines) {
            engineRepository.deleteById(engine.getId());
        }
    }

    @Test
    public void whenSaveThenGetSame() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        assertThat(carRepository.save(car)).isEqualTo(car);
    }

    @Test
    public void whenUpdateThenGetTrue() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        var savedTask = carRepository.save(car);

        var car2 = new Car();
        car2.setName("car2");
        car2.setId(savedTask.getId());
        car2.setEngine(engine);
        car2.setBrand(brand);

        assertThat(carRepository.update(car2)).isTrue();
        assertThat(carRepository.findById(car2.getId()).get()).isEqualTo(car2);
    }

    @Test
    public void whenUpdateThenGetFalse() {
        var car2 = new Car();
        car2.setName("car2");
        car2.setEngine(engine);
        car2.setBrand(brand);
        assertThat(carRepository.update(car2)).isFalse();
    }

    @Test
    public void whenDeleteThenGetTrue() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        carRepository.save(car);
        assertThat(carRepository.deleteById(car.getId())).isTrue();
    }

    @Test
    public void whenDeleteThenGetFalse() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        assertThat(carRepository.deleteById(car.getId())).isFalse();
    }

    @Test
    public void whenFindAllThenGetSame() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        List<Car> expected = List.of(car);
        carRepository.save(car);
        assertThat(carRepository.findAll()).isEqualTo(expected);
    }

    @Test
    public void whenFindByIdThenGetSame() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        carRepository.save(car);
        assertThat(carRepository.findById(car.getId()).get()).isEqualTo(car);
    }

    @Test
    public void whenFindByIdThenGetNothing() {
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBrand(brand);
        assertThat(carRepository.findById(car.getId())).isEmpty();
    }
}