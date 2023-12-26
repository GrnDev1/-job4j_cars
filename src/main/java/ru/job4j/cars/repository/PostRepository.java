package ru.job4j.cars.repository;

import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    AutoPost save(AutoPost post);

    boolean update(AutoPost post);

    boolean deleteById(int id);

    List<AutoPost> findAll();

    Optional<AutoPost> findById(int id);

    List<AutoPost> findAllForLastDay();

    List<AutoPost> findAllWithPhoto();

    List<AutoPost> findAllByBrand(Brand brand);
}