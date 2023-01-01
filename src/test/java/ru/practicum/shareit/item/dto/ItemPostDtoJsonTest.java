package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemPostDtoJsonTest {

    @Autowired
    JacksonTester<ItemPostDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemPostDto dto = TestUtils.getItem1PostDto();

        JsonContent<ItemPostDto> content = json.write(dto);

        assertThat(content).hasJsonPathStringValue("$.name");

        assertThat(content).doesNotHaveJsonPath("$.id");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"name\": \"item1_name\"}";

        ItemPostDto dto = json.parse(content).getObject();

        assertThat(dto.getName()).isEqualTo("item1_name");
    }

}
