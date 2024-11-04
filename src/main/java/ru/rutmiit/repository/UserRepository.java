package ru.rutmiit.repository;

import ru.rutmiit.domain.SessionRegistration;
import ru.rutmiit.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepository {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
