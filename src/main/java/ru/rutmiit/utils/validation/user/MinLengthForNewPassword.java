package ru.rutmiit.utils.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.rutmiit.utils.validation.email.UniqueEmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = MinLengthForNewPasswordValidator.class)
public @interface MinLengthForNewPassword {
    String message() default "Пароль должен содержать не менее {value} символов";

    int value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
