package ru.rutmiit.repositories.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Role;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.RoleRepository;

import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends BaseRepository<Role, UUID> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }
}
