package ru.rutmiit.exceptions.session;

public class SessionNotFoundException extends RuntimeException{

    public SessionNotFoundException(String message) {
        super(message);
    }
}
