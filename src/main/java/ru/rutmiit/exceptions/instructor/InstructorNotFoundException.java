package ru.rutmiit.exceptions.instructor;

public class InstructorNotFoundException extends RuntimeException {

    public InstructorNotFoundException(String message) {
        super(message);
    }
}
