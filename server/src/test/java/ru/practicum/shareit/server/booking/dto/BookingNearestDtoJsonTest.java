package ru.practicum.shareit.server.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.server.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingNearestDtoJsonTest {

    @Autowired
    JacksonTester<BookingNearestDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = TestUtils.getBookingNearestDto();
        var content = json.write(dto);

        assertThat(content).hasJsonPathNumberValue("$.id");
        assertThat(content).hasJsonPathNumberValue("$.bookerId");
    }

    @Test
    public void testDeserialize() throws Exception {
        var content = "{\"id\": 1, \"bookerId\": 1}";
        var dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getBookerId()).isEqualTo(1L);
    }

}