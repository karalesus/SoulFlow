package ru.rutmiit.repository.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Role;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.RoleRepository;

import java.util.UUID;

@Repository
public class RoleRepositoryImpl extends BaseRepository<Role, UUID> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }
}
