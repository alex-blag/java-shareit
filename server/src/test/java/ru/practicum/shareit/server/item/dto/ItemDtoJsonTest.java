package ru.practicum.shareit.server.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.server.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoJsonTest {

    @Autowired
    JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemDto dto = TestUtils.getItem1Dto();

        JsonContent<ItemDto> content = json.write(dto);

        assertThat(content).hasJsonPathNumberValue("$.id");
        assertThat(content).hasJsonPathStringValue("$.name");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\": 1, \"name\": \"item1_name\"}";

        ItemDto dto = json.parse(content).getObject();

        assertThat(dto.getId()).isEqualTo(1);
        assertThat(dto.getName()).isEqualTo("item1_name");
    }

}
