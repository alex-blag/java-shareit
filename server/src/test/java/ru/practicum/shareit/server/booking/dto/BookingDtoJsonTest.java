package ru.practicum.shareit.server.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingDto bookingDto = TestUtils.getBooking1Dto();

        JsonContent<BookingDto> content = json.write(bookingDto);

        assertThat(content).hasJsonPathNumberValue("$.id");
        assertThat(content).hasJsonPathStringValue("$.status");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\": 1, \"status\": \"WAITING\"}";

        BookingDto bookingDto = json.parse(content).getObject();

        assertThat(bookingDto.getId()).isEqualTo(1L);
        assertThat(bookingDto.getStatus()).isEqualTo(Status.WAITING);
    }

}