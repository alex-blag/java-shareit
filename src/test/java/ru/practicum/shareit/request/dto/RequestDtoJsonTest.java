package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestDtoJsonTest {

    @Autowired
    JacksonTester<RequestDto> json;

    @Test
    void testSerialize() throws Exception {
        RequestDto dto = TestUtils.getRequest1Dto();

        JsonContent<RequestDto> content = json.write(dto);

        assertThat(content).hasJsonPathNumberValue("$.id");
        assertThat(content).hasJsonPathStringValue("$.description");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\": 1, \"description\": \"request1_description\"}";

        RequestDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getDescription()).isEqualTo("request1_description");
    }

}
