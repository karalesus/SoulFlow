package ru.rutmiit.exceptions.review;

public class NotAttendedToPostReviewException extends RuntimeException{

    public NotAttendedToPostReviewException(String message) {
        super(message);
    }
}
