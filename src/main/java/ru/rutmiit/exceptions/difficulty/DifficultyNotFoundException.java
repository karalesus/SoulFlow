package ru.rutmiit.exceptions.difficulty;

public class DifficultyNotFoundException extends RuntimeException{

    public DifficultyNotFoundException(String message) {
        super(message);
    }
}
