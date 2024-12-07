package ru.rutmiit.exceptions.sessionRegistration;

public class NoAvailableSpotsException extends RuntimeException{

    public NoAvailableSpotsException(String message) {
        super(message);
    }
}
