package ru.rutmiit.exceptions.sessionRegistration;

public class AlreadyRegisteredException extends RuntimeException{

    public AlreadyRegisteredException(String message) {
        super(message);
    }
}
