package ru.rutmiit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rutmiit.exceptions.instructor.InstructorNotFoundException;
import ru.rutmiit.exceptions.session.InvalidSessionDataException;
import ru.rutmiit.exceptions.session.SessionNotFoundException;
import ru.rutmiit.exceptions.sessionRegistration.AlreadyRegisteredException;
import ru.rutmiit.exceptions.sessionRegistration.SessionRegistrationsNotFound;
import ru.rutmiit.exceptions.user.*;

@ControllerAdvice
public class CustomExceptionController {

    @ExceptionHandler(InvalidSessionDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidSessionDataHandler(InvalidSessionDataException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidEmailExceptionDataHandler(InvalidEmailException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidPasswordExceptionDataHandler(InvalidPasswordException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String InvalidUserDataExceptionDataHandler(InvalidUserDataException ex) {
        return ex.getMessage();
    }
    @ExceptionHandler(PasswordsNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String PasswordsNotMatchExceptionDataHandler(PasswordsNotMatchException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String UserNotFoundExceptionDataHandler(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SessionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String SessionNotFoundDataHandler(SessionNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(SessionRegistrationsNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String SessionRegistrationsNotFoundDataHandler(SessionRegistrationsNotFound ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String AlreadyRegisteredDataHandler(AlreadyRegisteredException ex , RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        return "redirect:/schedule/upcoming";
    }
    @ExceptionHandler(InstructorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String InstructorNotFoundExceptionDataHandler(InstructorNotFoundException ex) {
        return ex.getMessage();
    }
}
