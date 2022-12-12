package ru.practicum.shareit.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    public static boolean isStringNotBlank(String s) {
        return s != null && !s.isBlank();
    }

}
