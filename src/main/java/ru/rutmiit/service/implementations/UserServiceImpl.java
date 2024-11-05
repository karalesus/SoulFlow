package ru.rutmiit.service.implementations;

import jakarta.validation.ConstraintViolation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rutmiit.domain.Role;
import ru.rutmiit.domain.User;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.user.InvalidEmailException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.exceptions.user.UserNotFoundException;
import ru.rutmiit.repository.implementations.RoleRepositoryImpl;
import ru.rutmiit.repository.implementations.UserRepositoryImpl;
import ru.rutmiit.service.UserService;
import ru.rutmiit.utils.modelMapper.Mapper;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final Mapper mapper;
    private RoleRepositoryImpl roleRepository;
    private UserRepositoryImpl userRepository;
    private ValidationUtil validationUtil;

    public UserServiceImpl(Mapper mapper, RoleRepositoryImpl roleRepository, UserRepositoryImpl userRepository, ValidationUtil validationUtil) {
        this.mapper = mapper;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
    }

    @Override
    public void register(UserDTO userDTO) {
        if (!this.validationUtil.isValid(userDTO)) {

            this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidUserDataException("Данные пользователя введены неверно!");
        }
        boolean existsByEmail = userRepository.existsByEmail(userDTO.getEmail());

        if (existsByEmail) {
            throw new InvalidEmailException("Пользователь с таким email существует");
        }

        Optional<Role> role = roleRepository.findByName("MEMBER");
        Role userRole = role.orElseThrow();

        User user = mapper.convertUserDtoToUser(userDTO);
        user.setRole(List.of(userRole));
        userRepository.save(user);
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
    @Transactional
    public UserDTO editUser(UserDTO userDTO) {
        if (!this.validationUtil.isValid(userDTO)) {

            this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);
            throw new InvalidSessionDataException("Данные пользователя введены неверно!");
        }
        User user = userRepository.findByEmail(userDTO.getEmail()).orElseThrow(() ->
                new InvalidEmailException("Пользователь с email " + userDTO.getEmail() + " не найден.")
        );

        user.setName(userDTO.getName());
        this.userRepository.update(user);
        return mapper.convertUserToUserDto(user);
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
}
