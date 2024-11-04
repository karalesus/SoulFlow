package ru.rutmiit.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.rutmiit.domain.User;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.repository.implementations.UserRepositoryImpl;

@Component
public class Mapper {
    private final ModelMapper modelMapper;
    private UserRepositoryImpl userRepository;

    public Mapper(ModelMapper modelMapper, UserRepositoryImpl userRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public User convertUserDtoToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    public UserDTO convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
