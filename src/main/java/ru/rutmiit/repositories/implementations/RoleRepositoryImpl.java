package ru.rutmiit.repositories.implementations;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Role;
import ru.rutmiit.models.UserRoles;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends BaseRepository<Role, UUID> implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public Optional<Role> findRoleByName(UserRoles name) {
        List<Role> result = entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", name)
                .getResultList();

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }
}
