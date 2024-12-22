package ru.rutmiit.repositories;

import ru.rutmiit.models.Role;
import ru.rutmiit.models.UserRoles;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findRoleByName(UserRoles role);
}
