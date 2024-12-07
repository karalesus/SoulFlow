package ru.rutmiit.service;

import ru.rutmiit.dto.EditUserDTO;
import ru.rutmiit.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String register(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    EditUserDTO editUser(EditUserDTO userDTO);

    UserDTO getUserById(UUID uuid);
}
