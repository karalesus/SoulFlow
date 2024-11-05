package ru.rutmiit.exceptions.user;

public class InvalidUserDataException extends RuntimeException{

    public InvalidUserDataException(String message) {
        super(message);
    }
}
