package ru.practicum.shareit.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

    public static UserNotFoundException getUserNotFoundException(long userId) {
        String message = String.format("%s [userId = %d]", ExceptionMessage.USER_NOT_FOUND, userId);
        return new UserNotFoundException(message);
    }

    public static UserNotOwnItemException getUserNotOwnItemException(long userId, long itemId) {
        String message = String.format("%s [userId = %d, itemId = %d]", ExceptionMessage.USER_NOT_OWN_ITEM, userId, itemId);
        return new UserNotOwnItemException(message);
    }

    public static ItemNotFoundException getItemNotFoundException(long itemId) {
        String message = String.format("%s [itemId = %d]", ExceptionMessage.ITEM_NOT_FOUND, itemId);
        return new ItemNotFoundException(message);
    }

    public static EmailAlreadyExistsException getEmailAlreadyExistsException(String email) {
        String message = String.format("%s [email = %s]", ExceptionMessage.EMAIL_ALREADY_EXISTS, email);
        return new EmailAlreadyExistsException(message);
    }

}
