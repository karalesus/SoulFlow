package ru.rutmiit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.rutmiit.dto.Review.ReviewOutputDTO;
import ru.rutmiit.dto.SessionRegistrationDTO;
import ru.rutmiit.exceptions.review.InvalidReviewDataException;
import ru.rutmiit.models.*;
import ru.rutmiit.dto.Session.SessionInputDTO;
import ru.rutmiit.dto.UserDTO;
import ru.rutmiit.exceptions.instructor.InvalidInstructorDataException;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.user.InvalidUserDataException;
import ru.rutmiit.repositories.implementations.*;
import ru.rutmiit.service.implementations.*;

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
    private final StatusRepositoryImpl statusRepository;
    private final InstructorServiceImpl instructorService;
    private final SessionServiceImpl sessionService;
    private final SessionRegistrationServiceImpl sessionRegistrationService;
    private final ReviewServiceImpl reviewService;
    @Autowired
    public ApplicationCommandLineRunner(UserServiceImpl userService, UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, TypeRepositoryImpl typeRepository, DifficultyRepositoryImpl difficultyRepository, StatusRepositoryImpl statusRepository, InstructorServiceImpl instructorService, SessionServiceImpl sessionService, SessionRegistrationServiceImpl sessionRegistrationService, ReviewServiceImpl reviewService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.typeRepository = typeRepository;
        this.difficultyRepository = difficultyRepository;
        this.statusRepository = statusRepository;
        this.instructorService = instructorService;
        this.sessionService = sessionService;
        this.sessionRegistrationService = sessionRegistrationService;
        this.reviewService = reviewService;
    }

    @Override
    public void run(String... args) throws Exception {
        initRoles();
        initAdmin();
        initTypes();
        initDifficulties();
        initStatus();
//
//        while (true) {
//            System.out.println("Выберите опцию:" +
//                    "\n1 - добавить участника" +
//                    "\n2 - посмотреть участников" +
//                    "\n3 - обновить участника" +
//                    "\n4 - добавить инструктора"+
//                    "\n5 - добавить занятие"+
//                    "\n6 - получить ближайшие занятия"+
//                    "\n7 - посмотреть все занятия" +
//                    "\n8 - добавить отзыв" +
//                    "\n9 - получить рейтинг инструктора" +
//                    "\n10 - записаться на занятие");
//
//
//            String input = bufferedReader.readLine().toLowerCase();
//
//            switch (input) {
//                case "1":
//                    this.register();
//                    break;
//                case "2":
//                    this.showAllUsers();
//                    break;
//                case "3":
//                    this.editUser();
//                    break;
//                case "4":
//                    this.addInstructor();
//                    break;
//                case "5":
//                    this.addSession();
//                    break;
//                case "6":
//                    this.getUpcomingSessions();
//                    break;
//                case "7":
//                    this.getAllSessions();
//                    break;
//                case "8":
//                    this.addReview();
//                    break;
//                case "9":
//                    this.getRatingByInstructor();
//                    break;
//                case "10":
//                    this.addSessionRegistration();
//                    break;
//            }
//            System.out.println("==================================");
//        }


}

//    private void addSessionRegistration() throws IOException {
//        System.out.println("Введите данные в таком формате: id_участника id_занятия");
//        String[] params = bufferedReader.readLine().split(" ");
//        SessionRegistrationDTO sessionRegistrationDTO = new SessionRegistrationDTO(params[0], params[1]);
//        try {
//            this.sessionRegistrationService.addSessionRegistration(sessionRegistrationDTO);
//            System.out.println("Вы успешно записаны!");
//        }catch (InvalidInstructorDataException e) {
//            System.out.println("Ошибка записи на занятие: " + e.getMessage());
//        }
//    }

    private void getRatingByInstructor() {
    }

//    private void addReview() throws IOException {
//        System.out.println("Введите отзыв в таком формате: id_участника;id_занятия;рейтинг(1-5);комментарий");
//        String[] reviewParams = bufferedReader.readLine().split(";");
//        ReviewOutputDTO reviewOutputDTO = new ReviewOutputDTO(reviewParams[0], reviewParams[1], Integer.parseInt(reviewParams[2]), reviewParams[3]);
//        try {
//            this.reviewService.addReview(reviewOutputDTO);
//            System.out.println("Отзыв успешно добавлен!");
//        }catch (InvalidReviewDataException e) {
//            System.out.println("Ошибка добавления отзыва: " + e.getMessage());
//        }
//    }

//    private void getUpcomingSessions() {
//        LocalDateTime now = LocalDateTime.now();
//        List<SessionInputDTO> sessionsList = this.sessionService
//                .getUpcomingSessions(now);
//        sessionsList.forEach(s -> System.out.printf("Занятие: %s Дата и время: %s Инструктор: %s\n", s.getName(), s.getDateTime(), s.getInstructor()));
//    }

    private void getAllSessions() {
        List<SessionInputDTO> sessionsList = this.sessionService
                .getAllSessions();

        sessionsList.forEach(s -> System.out.printf("Занятие: %s Дата и время: %s Инструктор: %s\n", s.getName(), s.getDateTime(), s.getInstructor()));
    }

//    private void addSession() throws IOException {
//        // Хатха-йога для спокойствия души,120,пример описания,2024-11-06 12:00,20,1500,начинающий,хатха,Трофимова Анна Владимировна
//        System.out.println("Введите данные занятия в формате: имя,длительность,описание,дата_время (yyyy-MM-dd HH:mm),макс_вместимость,цена,сложность,тип,инструктор");
//        String[] sessionParams = bufferedReader.readLine().split(";");
//
//        try {
//            LocalDateTime dateTime = LocalDateTime.parse(sessionParams[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
//            SessionInputDTO sessionInputDTO = new SessionInputDTO(
//                    sessionParams[0],
//                    Integer.parseInt(sessionParams[1]),
//                    sessionParams[2],
//                    dateTime, // Дата в формате "yyyy-MM-dd HH:mm"
//                    Integer.parseInt(sessionParams[4]),
//                    Integer.parseInt(sessionParams[5]),
//                    sessionParams[6],
//                    sessionParams[7],
//                    sessionParams[8]
//            );
//            this.sessionService.addSession(sessionInputDTO);
//            System.out.println("Занятие успешно добавлено!");
//        } catch (DateTimeParseException e) {
//            System.out.println("Ошибка: неверный формат даты. Используйте 'yyyy-MM-dd HH:mm'.");
//        } catch (InvalidSessionDataException e) {
//            System.out.println("Ошибка добавления занятия: " + e.getMessage());
//        }
//    }


//    private void addInstructor() throws IOException {
//        System.out.println("Введите данные пользователя в таком формате: имя,сертификат,ссылка_на_фото");
//        String[] instructorParams = bufferedReader.readLine().split(",");
//        InstructorDTO instructorDTO = new InstructorDTO(instructorParams[0], instructorParams[1], instructorParams[2]);
//        try {
//            this.instructorService.addInstructor(instructorDTO);
//            System.out.println("Инструктор успешно добавлен!");
//        }catch (InvalidInstructorDataException e) {
//            System.out.println("Ошибка добавления инструктора: " + e.getMessage());
//        }
//    }

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

//    private void editUser() throws IOException {
//        System.out.println("Введите данные пользователя в таком формате: новое_имя существующий_email пароль");
//        String[] userParams = bufferedReader.readLine().split("\\s+");
//
//        UserDTO userDTO = new UserDTO(userParams[0], userParams[1], userParams[2]);
//
//        try {
//            UserDTO updatedUser = userService.editUser(userDTO);
//            System.out.println("Пользователь успешно обновлен: " + updatedUser);
//        } catch (InvalidUserDataException e) {
//            System.out.println("Ошибка при обновлении пользователя: " + e.getMessage());
//        }
//    }

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

    private void initStatus() {
        List<String> statusNames = List.of("Записан", "Отменено", "Посещено");

        for (String statusName : statusNames) {
            if (!statusRepository.existsByName(statusName)) {
                statusRepository.save(new Status(statusName));
            }
        }
    }
}
