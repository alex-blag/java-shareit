package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExceptionUtilsTest {

    @Test
    void getUserNotOwnItemException_expectCorrect() {
        assertNotNull(ExceptionUtils.getUserNotOwnItemException(1L, 1L));
    }

    @Test
    void getEmailAlreadyExistsException_expectCorrect() {
        assertNotNull(ExceptionUtils.getEmailAlreadyExistsException("user1@mail.com"));
    }

    @Test
    void getItemNotAvailableException_expectCorrect() {
        assertNotNull(ExceptionUtils.getItemNotAvailableException(1L));
    }

    @Test
    void getBookingAlreadyApprovedException_expectCorrect() {
        assertNotNull(ExceptionUtils.getBookingAlreadyApprovedException(1L));
    }

    @Test
    void getBookerOwnsItemException_expectCorrect() {
        assertNotNull(ExceptionUtils.getBookerOwnsItemException(1L, 1L));
    }

    @Test
    void getUserNotBookedItemException_expectCorrect() {
        assertNotNull(ExceptionUtils.getUserNotBookedItemException(1L, 1L));
    }

}