package ru.job4j.cars.repository;

import ru.job4j.cars.model.AutoPost;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    AutoPost save(AutoPost post);

    boolean update(AutoPost post);

    boolean deleteById(int id);

    List<AutoPost> findAll();

    Optional<AutoPost> findById(int id);
}