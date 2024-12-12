package ru.rutmiit.utils.validation.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.rutmiit.repositories.implementations.UserRepositoryImpl;

public class UniqueEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    private UserRepositoryImpl userRepository;

    @Autowired
    public void setUserRepository(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByEmail(email);
    }
}
