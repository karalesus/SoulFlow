package ru.rutmiit.repositories;

import ru.rutmiit.models.User;

import java.util.Optional;


public interface UserRepository {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
