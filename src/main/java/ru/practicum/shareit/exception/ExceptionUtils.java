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
        String message = String.format(
                "%s [userId = %d, itemId = %d]",
                ExceptionMessage.USER_NOT_OWN_ITEM,
                userId,
                itemId
        );
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

    public static ItemNotAvailableException getItemNotAvailableException(long itemId) {
        String message = String.format("%s [itemId = %d]", ExceptionMessage.ITEM_NOT_AVAILABLE, itemId);
        return new ItemNotAvailableException(message);
    }

    public static BookingNotFoundException getBookingNotFoundException(long bookingId) {
        String message = String.format("%s [bookingId = %d]", ExceptionMessage.BOOKING_NOT_FOUND, bookingId);
        return new BookingNotFoundException(message);
    }

    public static UserNotRelatedWithBookingException getUserNotRelatedWithBookingException(long userId, long bookingId) {
        String message = String.format(
                "%s [userId = %d, bookingId = %d]",
                ExceptionMessage.USER_NOT_RELATED_WITH_BOOKING,
                userId,
                bookingId
        );
        return new UserNotRelatedWithBookingException(message);
    }

    public static BookingStateUnknownException getBookingStateUnknownException(String state) {
        String message = String.format("%s [state = %s]", ExceptionMessage.BOOKING_STATE_UNKNOWN, state);
        return new BookingStateUnknownException(message, state);
    }

    public static BookingAlreadyApprovedException getBookingAlreadyApprovedException(long bookingId) {
        String message = String.format("%s [bookingId = %d]", ExceptionMessage.BOOKING_ALREADY_APPROVED, bookingId);
        return new BookingAlreadyApprovedException(message);
    }

    public static BookerOwnsItemException getBookerOwnsItemException(long bookerId, long itemId) {
        String message = String.format(
                "%s [bookerId = %d, itemId = %d]",
                ExceptionMessage.BOOKER_OWNS_ITEM,
                bookerId,
                itemId
        );
        return new BookerOwnsItemException(message);
    }

    public static UserNotBookedItemException getUserNotBookedItemException(long userId, long itemId) {
        String message = String.format(
                "%s [userId = %d, itemId = %d]",
                ExceptionMessage.USER_NOT_BOOKED_ITEM,
                userId,
                itemId
        );
        return new UserNotBookedItemException(message);
    }

    public static RequestNotFoundException getRequestNotFoundException(long requestId) {
        String message = String.format("%s [requestId = %d]", ExceptionMessage.REQUEST_NOT_FOUND, requestId);
        return new RequestNotFoundException(message);
    }

}
