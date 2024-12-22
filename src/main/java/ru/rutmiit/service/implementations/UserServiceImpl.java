package ru.rutmiit.service.implementations;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rutmiit.dto.user.EditUserDTO;
import ru.rutmiit.exceptions.user.*;
import ru.rutmiit.models.User;
import ru.rutmiit.dto.user.UserDTO;
import ru.rutmiit.models.UserRoles;
import ru.rutmiit.repositories.implementations.RoleRepositoryImpl;
import ru.rutmiit.repositories.implementations.UserRepositoryImpl;
import ru.rutmiit.service.UserService;
import ru.rutmiit.utils.validation.ValidationUtil;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private RoleRepositoryImpl roleRepository;
    private UserRepositoryImpl userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
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
    @Transactional
    public String register(UserDTO userDTO) {
        if (!this.validationUtil.isValid(userDTO)) {
            String constraintViolations = this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new InvalidUserDataException(constraintViolations);
        }

        boolean existsByEmail = userRepository.existsByEmail(userDTO.getEmail());

        if (existsByEmail) {
            throw new InvalidEmailException("Пользователь с таким email существует");
        }

        var userRole = roleRepository.
                findRoleByName(UserRoles.MEMBER).orElseThrow();

        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            throw new PasswordsNotMatchException("Пароли не совпадают");
        }

        User user = new User(
                userDTO.getName(),
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword())
        );
        user.setRoles(List.of(userRole));
        return userRepository.save(user).getId().toString();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> usersList = userRepository.findAll().stream().map(this::convertUserToUserDto).collect(Collectors.toList());
        if (usersList.isEmpty()) {
            throw new UserNotFoundException("Пользователей не найдено");
        } else {
            return usersList;
        }
    }

    @Override
    public EditUserDTO editUser(EditUserDTO userDTO) {
        validateUser(userDTO);
        User user = userRepository.findById(UUID.fromString(userDTO.getId())).orElseThrow(() ->
                new InvalidEmailException("Пользователь с id " + userDTO.getId() + " не найден.")
        );

        if (!passwordEncoder.matches(userDTO.getCurrentPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Текущий пароль указан неверно!");
        }

        user.setName(userDTO.getName());

        if (!userDTO.getNewPassword().isEmpty()) {
            if (!userDTO.getConfirmNewPassword().equals(userDTO.getNewPassword())) {
                throw new PasswordsNotMatchException("Пароли не совпадают");
            }
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }

        this.userRepository.save(user);
        return convertEditUserToUserDto(user);
    }

    @Override
    public UserDTO getUserById(UUID uuid) {
        if (uuid == null) {
            throw new InvalidUserDataException("Ошибка ID пользователя");
        }

        return userRepository
                .findById(uuid)
                .map(this::convertUserToUserDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email + " не найден"));
    }

    private void validateUser(EditUserDTO userDTO) {
        if (!this.validationUtil.isValid(userDTO)) {
            String constraintViolations = this.validationUtil
                    .violations(userDTO)
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new InvalidUserDataException(constraintViolations);
        }
    }

    public UserDTO convertUserToUserDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public EditUserDTO convertEditUserToUserDto(User user) {
        return modelMapper.map(user, EditUserDTO.class);
    }


}
