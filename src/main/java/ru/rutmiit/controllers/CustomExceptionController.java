package ru.rutmiit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;

@ControllerAdvice
public class CustomExceptionController {

    @ResponseBody
    @ExceptionHandler(InvalidSessionDataException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String InvalidSessionDataHandler(InvalidSessionDataException ex) {
        return ex.getMessage();
    }
}
