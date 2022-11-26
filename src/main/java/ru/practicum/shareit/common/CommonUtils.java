package ru.practicum.shareit.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static boolean isStringNotBlank(String s) {
        return s != null && !s.isBlank();
    }

}
