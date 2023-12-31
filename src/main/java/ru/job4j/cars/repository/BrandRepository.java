package ru.job4j.cars.repository;

import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Brand save(Brand brand);

    List<Brand> findAll();

    Optional<Brand> findById(int id);

    boolean deleteById(int id);
}