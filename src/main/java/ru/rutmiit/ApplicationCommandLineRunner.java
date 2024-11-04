package ru.rutmiit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.rutmiit.domain.Role;
import ru.rutmiit.domain.User;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.InvalidUserDataException;
import ru.rutmiit.repository.implementations.*;
import ru.rutmiit.service.implementations.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    private final BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));

    private final UserServiceImpl userService;
    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;

    @Autowired
    public ApplicationCommandLineRunner(UserServiceImpl userService, UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initAdmin();

        while (true) {
            System.out.println("Выберите опцию:" +
                    "\n1 - добавить участника" +
                    "\n2 - посмотреть участников" +
                    "\n3 - обновить участника");


            String input = bufferedReader.readLine().toLowerCase();

            switch (input) {
                case "1":
                    this.register();
                    break;
                case "2":
                    this.showAllUsers();
                    break;
                case "3":
                    this.editUser();
                    break;
            }
            System.out.println("==================================");
        }
    }

    private void register() throws IOException {
        System.out.println("Введите данные пользователя в таком формате: имя email пароль");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserDTO userDTO = new UserDTO(userParams[0], userParams[1], userParams[2]);

        try {
            this.userService.register(userDTO);
            System.out.println("Участник добавлен!");
        } catch (InvalidUserDataException e) {
            System.out.println("Ошибка добавления пользователя: " + e.getMessage());
        }
    }

    private void editUser() throws IOException {
        System.out.println("Введите данные пользователя в таком формате: новое_имя существующий_email пароль");
        String[] userParams = bufferedReader.readLine().split("\\s+");

        UserDTO userDTO = new UserDTO(userParams[0], userParams[1], userParams[2]);

        try {
            UserDTO updatedUser = userService.editUser(userDTO);
            System.out.println("Пользователь успешно обновлен: " + updatedUser);
        } catch (InvalidUserDataException e) {
            System.out.println("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }

    private void showAllUsers() throws IOException {
        List<UserDTO> usersList = this.userService
                .getAllUsers();

        usersList.forEach(s -> System.out.printf("Пользователь: %s Почта: %s\n", s.getName(), s.getEmail()));
    }

    private void initRoles() {
        if (roleRepository.findByName("ADMIN").isPresent() && roleRepository.findByName("MEMBER").isPresent()) {
            System.out.println("Роли уже проинициализированы.");
            return;
        }
        Role adminRole = new Role("ADMIN");
        Role memberRole = new Role("MEMBER");
        roleRepository.save(adminRole);
        roleRepository.save(memberRole);

    }

    private void initAdmin() {
        if (userRepository.existsByEmail("admin@mail.ru")) {
            System.out.println("Администратор уже существует.");
            return;
        }
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();

        User adminUser = new User("admin", "admin@mail.ru", "123");
        adminUser.setRole(List.of(adminRole));

        userRepository.save(adminUser);
    }
}
