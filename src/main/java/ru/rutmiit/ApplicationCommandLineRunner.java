package ru.rutmiit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.rutmiit.models.*;
import ru.rutmiit.repositories.implementations.*;
import ru.rutmiit.service.implementations.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {
    private final UserRepositoryImpl userRepository;
    private final RoleRepositoryImpl roleRepository;
    private final TypeRepositoryImpl typeRepository;
    private final DifficultyRepositoryImpl difficultyRepository;
    private final StatusRepositoryImpl statusRepository;
    private final PasswordEncoder passwordEncoder;
    private final String defaultPassword;

    @Autowired
    public ApplicationCommandLineRunner(UserRepositoryImpl userRepository, RoleRepositoryImpl roleRepository, TypeRepositoryImpl typeRepository, DifficultyRepositoryImpl difficultyRepository, StatusRepositoryImpl statusRepository, PasswordEncoder passwordEncoder, @Value("${app.default.password}") String defaultPassword) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.typeRepository = typeRepository;
        this.difficultyRepository = difficultyRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
        this.defaultPassword = defaultPassword;
    }

    @Override
    public void run(String... args) {
        initRoles();
        initAdmin();
        initTypes();
        initDifficulties();
        initStatus();
    }

    private void initRoles() {
        if (roleRepository.findRoleByName(UserRoles.ADMIN).isPresent() && roleRepository.findRoleByName(UserRoles.MEMBER).isPresent()) {
            System.out.println("Роли уже проинициализированы.");
            return;
        }
        Role adminRole = new Role(UserRoles.ADMIN);
        Role memberRole = new Role(UserRoles.MEMBER);
        roleRepository.save(adminRole);
        roleRepository.save(memberRole);

    }

    private void initAdmin() {
        if (userRepository.existsByEmail("admin@mail.ru")) {
            System.out.println("Администратор уже существует.");
            return;
        }
        var adminRole = roleRepository.findRoleByName(UserRoles.ADMIN).orElseThrow();

        User adminUser = new User("Admin Adminovich", "admin@mail.ru", passwordEncoder.encode(defaultPassword));
        adminUser.setRoles(List.of(adminRole));

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
