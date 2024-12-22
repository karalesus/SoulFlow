package ru.rutmiit.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.rutmiit.models.User;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, UUID> implements UserRepository {

    @PersistenceContext
    EntityManager entityManager;

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    @Transactional
    public Optional<User> findByEmail(String email) {
        return entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

}
