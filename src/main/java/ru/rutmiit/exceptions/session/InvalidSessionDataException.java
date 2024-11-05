package ru.rutmiit.exceptions.session;

public class InvalidSessionDataException extends RuntimeException{

    public InvalidSessionDataException(String message) {
        super(message);
    }
}
