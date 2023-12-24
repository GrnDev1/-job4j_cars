package ru.job4j.cars.repository;

import ru.job4j.cars.model.Owner;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository {
    Optional<Owner> save(Owner owner);

    boolean update(Owner owner);

    boolean deleteById(int id);

    List<Owner> findAll();

    Optional<Owner> findById(int id);
}