package ru.rutmiit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.rutmiit.domain.Difficulty;
import ru.rutmiit.domain.Role;
import ru.rutmiit.domain.Type;
import ru.rutmiit.domain.User;
import ru.rutmiit.dto.InstructorDTO;
import ru.rutmiit.dto.SessionDTO;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.instructor.InvalidInstructorDataException;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.repository.implementations.*;
import ru.rutmiit.service.implementations.InstructorServiceImpl;
import ru.rutmiit.service.implementations.SessionServiceImpl;
import ru.rutmiit.service.implementations.UserServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    private final BufferedReader bufferedReader =
            new BufferedReader(new InputStreamReader(System.in));

    private final UserServiceImpl userService;
    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;
    private final TypeRepositoryImpl typeRepository;
    private final DifficultyRepositoryImpl difficultyRepository;
    private final InstructorServiceImpl instructorService;
    private final SessionServiceImpl sessionService;
    @Autowired
    public ApplicationCommandLineRunner(UserServiceImpl userService, UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, TypeRepositoryImpl typeRepository, DifficultyRepositoryImpl difficultyRepository, InstructorServiceImpl instructorService, SessionServiceImpl sessionService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.typeRepository = typeRepository;
        this.difficultyRepository = difficultyRepository;
        this.instructorService = instructorService;
        this.sessionService = sessionService;
    }

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initAdmin();
        initTypes();
        initDifficulties();

        while (true) {
            System.out.println("Выберите опцию:" +
                    "\n1 - добавить участника" +
                    "\n2 - посмотреть участников" +
                    "\n3 - обновить участника" +
                    "\n4 - добавить инструктора"+
                    "\n5 - добавить занятие");


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
                case "4":
                    this.addInstructor();
                    break;
                case "5":
                    this.addSession();
                    break;
            }
            System.out.println("==================================");
        }
    }

    private void addSession() throws IOException {
        // Хатха-йога для спокойствия души,120,пример описания,2024-11-06 12:00,20,1500,начинающий,хатха,Трофимова Анна Владимировна
        System.out.println("Введите данные занятия в формате: имя,длительность,описание,дата_время (yyyy-MM-dd HH:mm),макс_вместимость,цена,сложность,тип,инструктор");
        String[] sessionParams = bufferedReader.readLine().split(",");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(sessionParams[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            SessionDTO sessionDTO = new SessionDTO(
                    sessionParams[0],
                    Integer.parseInt(sessionParams[1]),
                    sessionParams[2],
                    dateTime, // Дата в формате "yyyy-MM-dd HH:mm"
                    Integer.parseInt(sessionParams[4]),
                    Integer.parseInt(sessionParams[5]),
                    sessionParams[6],
                    sessionParams[7],
                    sessionParams[8]
            );
            this.sessionService.addSession(sessionDTO);
            System.out.println("Занятие успешно добавлено!");
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка: неверный формат даты. Используйте 'yyyy-MM-dd HH:mm'.");
        } catch (InvalidSessionDataException e) {
            System.out.println("Ошибка добавления занятия: " + e.getMessage());
        }
    }


    private void addInstructor() throws IOException {
        System.out.println("Введите данные пользователя в таком формате: имя,сертификат,ссылка_на_фото");
        String[] instructorParams = bufferedReader.readLine().split(",");
        InstructorDTO instructorDTO = new InstructorDTO(instructorParams[0], instructorParams[1], instructorParams[2]);
        try {
            this.instructorService.addInstructor(instructorDTO);
            System.out.println("Инструктор успешно добавлен!");
        }catch (InvalidInstructorDataException e) {
            System.out.println("Ошибка добавления инструктора: " + e.getMessage());
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
    private void initTypes() {
        List<String> typeNames = List.of("хатха", "медитация", "кундалини", "фитнес", "виньяса", "ваджра");

        for (String typeName : typeNames) {
            if (!typeRepository.existsByName(typeName)) {
                typeRepository.save(new Type(typeName));
            }
        }
    }
    private void initDifficulties() {
        List<String> difficultyNames = List.of("начинающий", "средний", "продвинутый");

        for (String difficultyName : difficultyNames) {
            if (!difficultyRepository.existsByName(difficultyName)) {
                difficultyRepository.save(new Difficulty(difficultyName));
            }
        }
    }
}
