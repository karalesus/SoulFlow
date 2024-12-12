package ru.rutmiit.exceptions.user;

public class PasswordsNotMatchException extends RuntimeException{

    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
