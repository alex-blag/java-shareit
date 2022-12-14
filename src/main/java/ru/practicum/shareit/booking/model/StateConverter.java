package ru.practicum.shareit.booking.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ExceptionUtils;

@Component
public class StateConverter implements Converter<String, State> {

    @Override
    public State convert(String s) {
        try {
            return State.valueOf(s);
        } catch (IllegalArgumentException e) {
            throw ExceptionUtils.getBookingStateUnknownException(s);
        }
    }

}
