package ru.rutmiit.utils.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MinLengthForNewPasswordValidator implements ConstraintValidator<MinLengthForNewPassword, String> {

    private int minLength;

    @Override
    public void initialize(MinLengthForNewPassword constraintAnnotation) {
        this.minLength = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
        return field.length() >= minLength || field.isEmpty();
    }
}
