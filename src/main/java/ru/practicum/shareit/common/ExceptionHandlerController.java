package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.UserNotOwnItemException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<String> handle(UserNotFoundException e) {
        log.error("", e);
        return getExceptionResponse(ExceptionMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(UserNotOwnItemException e) {
        log.error("", e);
        return getExceptionResponse(ExceptionMessage.USER_NOT_OWN_ITEM, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(EmailAlreadyExistsException e) {
        log.error("", e);
        return getExceptionResponse(ExceptionMessage.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        log.error("", e);
        return getExceptionResponse(
                buildFieldErrorsMessage(e.getBindingResult().getFieldErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Throwable e) {
        log.error("", e);
        return getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<String> getExceptionResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }

    private String buildFieldErrorsMessage(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fe -> {
            sb.append(fe.getField()).append(" ");
            sb.append(fe.getDefaultMessage()).append("\n");
        });
        return sb.toString();
    }

}
