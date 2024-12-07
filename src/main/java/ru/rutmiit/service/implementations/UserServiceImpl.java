package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.EditUserDTO;
import ru.rutmiit.models.Role;
import ru.rutmiit.models.User;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.user.InvalidEmailException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repositories.implementations.RoleRepositoryImpl;
import ru.rutmiit.repositories.implementations.UserRepositoryImpl;
import ru.rutmiit.service.UserService;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private ValidationUtil validationUtil;
    private final Mapper mapper;
    private RoleRepositoryImpl roleRepository;
    private UserRepositoryImpl userRepository;

    @Autowired
    public UserServiceImpl(Mapper mapper, ValidationUtil validationUtil) {
        this.mapper = mapper;
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setRoleRepository(RoleRepositoryImpl roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String register(UserDTO userDTO) {
        if (!this.validationUtil.isValid(userDTO)) {

            this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidSessionDataException("Некорректные данные пользователя при регистрации");
        }

        boolean existsByEmail = userRepository.existsByEmail(userDTO.getEmail());

        if (existsByEmail) {
            throw new InvalidEmailException("Пользователь с таким email существует");
        }

        Optional<Role> role = roleRepository.findByName("MEMBER");
        Role userRole = role.orElseThrow();

        User user = mapper.convertUserDtoToUser(userDTO);
        user.setRole(List.of(userRole));
        return userRepository.save(user).getId().toString();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> usersList = userRepository.findAll().stream().map(mapper::convertUserToUserDto).collect(Collectors.toList());
        if (usersList.isEmpty()) {
            throw new UserNotFoundException("Пользователей не найдено");
        } else {
            return usersList;
        }
    }

    @Override
    public EditUserDTO editUser(EditUserDTO userDTO) {
        validateUser(userDTO, "Некорректные данные пользователя при редактировании");
        User user = userRepository.findById(UUID.fromString(userDTO.getId())).orElseThrow(() ->
                new InvalidEmailException("Пользователь с sessionId " + userDTO.getId() + " не найден.")
        );

//        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
//            throw new
//        }
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

//        if (!newPassword.isBlank()) {
//            user.setPassword(passwordEncoder.encode(newPassword));
//        }
        this.userRepository.save(user);
        return mapper.convertEditUserToUserDto(user);
    }

    @Override
    public UserDTO getUserById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidUserDataException("Ошибка ID пользователя");
        }

        return userRepository
                .findById(uuid)
                .map(mapper::convertUserToUserDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    private void validateUser(EditUserDTO userDTO, String exceptionMessage) {
        if (!this.validationUtil.isValid(userDTO)) {

            this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidSessionDataException(exceptionMessage);
        }
    }
}
