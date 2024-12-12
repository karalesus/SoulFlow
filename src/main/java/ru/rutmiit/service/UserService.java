package ru.rutmiit.service;

import ru.rutmiit.dto.user.EditUserDTO;
import ru.rutmiit.dto.user.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    String register(UserDTO userDTO);
    List<UserDTO> getAllUsers();
    EditUserDTO editUser(EditUserDTO userDTO);

    UserDTO getUserById(UUID uuid);
}
