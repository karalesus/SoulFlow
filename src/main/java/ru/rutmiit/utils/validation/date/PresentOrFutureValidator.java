package ru.rutmiit.utils.validation.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class PresentOrFutureValidator implements ConstraintValidator<PresentOrFuture, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext constraintValidatorContext) {
        return !date.isBefore(LocalDateTime.now());
    }
}
