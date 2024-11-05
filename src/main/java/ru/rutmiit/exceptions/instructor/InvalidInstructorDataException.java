package ru.rutmiit.exceptions.instructor;

public class InvalidInstructorDataException extends RuntimeException{
    public InvalidInstructorDataException(String message) {
        super(message);
    }
}
