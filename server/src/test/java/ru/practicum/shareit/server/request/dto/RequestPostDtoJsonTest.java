package ru.practicum.shareit.server.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.server.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class RequestPostDtoJsonTest {

    @Autowired
    JacksonTester<RequestPostDto> json;

    @Test
    void testSerialize() throws Exception {
        RequestPostDto dto = TestUtils.getRequest1PostDto();

        JsonContent<RequestPostDto> content = json.write(dto);

        assertThat(content).hasJsonPathStringValue("$.description");

        assertThat(content).doesNotHaveJsonPath("$.id");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"description\": \"request1_description\"}";

        RequestPostDto dto = json.parse(content).getObject();

        assertThat(dto.getDescription()).isEqualTo("request1_description");
    }

}
