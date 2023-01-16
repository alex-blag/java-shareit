package ru.practicum.shareit.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler
    public ResponseEntity<String> handle(UserNotFoundException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(EmailAlreadyExistsException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(UserNotOwnItemException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.USER_NOT_OWN_ITEM, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(UserNotRelatedWithBookingException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.USER_NOT_RELATED_WITH_BOOKING, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(UserNotBookedItemException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.USER_NOT_BOOKED_ITEM, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(ItemNotFoundException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.ITEM_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(ItemNotAvailableException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.ITEM_NOT_AVAILABLE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(BookingNotFoundException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.BOOKING_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(BookingAlreadyApprovedException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.BOOKING_ALREADY_APPROVED, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(BookerOwnsItemException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.BOOKER_OWNS_ITEM, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(RequestNotFoundException e) {
        logError(e);
        return getExceptionResponse(ExceptionMessage.REQUEST_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(MethodArgumentNotValidException e) {
        logError(e);
        return getExceptionResponse(
                buildFieldErrorsMessage(e.getBindingResult().getFieldErrors()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(MethodArgumentTypeMismatchException e) {
        logError(e);
        ResponseEntity<String> response;
        Throwable rootCause = findRootCause(e);
        if (rootCause instanceof BookingStateUnknownException) {
            response = getBookingStateUnknownExceptionResponse((BookingStateUnknownException) rootCause);
        } else {
            response = getInternalServerErrorResponse();
        }
        return response;
    }

    private ResponseEntity<String> getBookingStateUnknownExceptionResponse(BookingStateUnknownException e) {
        return getExceptionResponse("{\"error\":\"Unknown state: " + e.getState() + "\"}", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handle(Throwable e) {
        logError(e);
        return getInternalServerErrorResponse();
    }

    private void logError(Throwable e) {
        log.error(e.getMessage(), e);
    }

    private ResponseEntity<String> getExceptionResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }

    private ResponseEntity<String> getInternalServerErrorResponse() {
        return getExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private Throwable findRootCause(Throwable e) {
        Throwable rootCause = e;
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }
        return rootCause;
    }

    private String buildFieldErrorsMessage(List<FieldError> fieldErrors) {
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fe -> sb.append(fe.getDefaultMessage()).append("\n"));
        return sb.toString();
    }

}
