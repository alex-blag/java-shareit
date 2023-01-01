package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionMessageTest {

    @Test
    void userNotFound_expectCorrect() {
        Assertions.assertNotNull(ExceptionMessage.USER_NOT_FOUND);
    }

}
