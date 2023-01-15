package ru.practicum.shareit.gateway.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.gateway.booking.dto.BookingPostDto;
import ru.practicum.shareit.gateway.booking.dto.BookingState;
import ru.practicum.shareit.gateway.client.BaseClient;

import java.util.Map;

import static ru.practicum.shareit.gateway.common.CommonUtils.BOOKINGS_RESOURCE;

@Service
public class BookingClient extends BaseClient {

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + BOOKINGS_RESOURCE))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> bookItem(long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> post(long userId, BookingPostDto bookingPostDto) {
        return post("/", userId, bookingPostDto);
    }

    public ResponseEntity<Object> patchByBookingId(long userId, long bookingId, boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );

        return patch("/" + bookingId + "?approved={approved}", userId, parameters);
    }

    public ResponseEntity<Object> getByIdAndOwnerIdOrBookerId(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllByBookerIdAndState(long userId, BookingState bookingState, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "state", bookingState.name(),
                "from", from,
                "size", size
        );

        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllByOwnerIdAndState(
            String ownerResource,
            long userId,
            BookingState bookingState,
            int from,
            int size
    ) {
        Map<String, Object> parameters = Map.of(
                "state", bookingState.name(),
                "from", from,
                "size", size
        );

        return get(ownerResource + "?state={state}&from={from}&size={size}", userId, parameters);
    }

}
