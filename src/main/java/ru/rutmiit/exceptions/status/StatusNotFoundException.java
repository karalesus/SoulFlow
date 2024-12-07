package ru.rutmiit.exceptions.status;

public class StatusNotFoundException extends RuntimeException{

    public StatusNotFoundException(String message) {
        super(message);
    }
}
