package ru.rutmiit.exceptions.review;

public class AlreadyPostedReviewException extends RuntimeException{
    public AlreadyPostedReviewException(String message) {
        super(message);
    }
}
