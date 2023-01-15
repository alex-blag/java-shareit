package ru.practicum.shareit.server.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    public static final String INTEGER_MAX_VALUE = "2147483647";

    public static boolean isStringNotBlank(String s) {
        return s != null && !s.isBlank();
    }

}
