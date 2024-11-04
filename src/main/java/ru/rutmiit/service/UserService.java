package ru.rutmiit.service;

import ru.rutmiit.domain.User;
import ru.rutmiit.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void register(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    UserDTO editUser(UserDTO userDTO);
    UserDTO getUserById(UUID uuid);
}
