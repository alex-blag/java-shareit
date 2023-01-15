package ru.practicum.shareit.gateway.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

    public static final String SHAREIT_SERVER_URL_PROPERTY = "${shareit-server.url}";

    public static final String X_SHARER_USER_ID = "X-Sharer-User-Id";

    public static final String USERS_RESOURCE = "/users";

    public static final String ITEMS_RESOURCE = "/items";

    public static final String BOOKINGS_RESOURCE = "/bookings";

    public static final String OWNER_RESOURCE = "/owner";

    public static final String REQUESTS_RESOURCE = "/requests";

    public static final String ALL_RESOURCE = "/all";

    public static final String INTEGER_MAX_VALUE = "2147483647";

    public static final String MAX_SIZE = INTEGER_MAX_VALUE;

}
