package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoJsonTest {

    @Autowired
    JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentDto commentDto = TestUtils.getComment1Dto();

        JsonContent<CommentDto> content = json.write(commentDto);

        assertThat(content).hasJsonPathNumberValue("$.id");
        assertThat(content).hasJsonPathStringValue("$.text");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\": 1, \"text\": \"comment_text\"}";

        CommentDto commentDto = json.parse(content).getObject();

        assertThat(commentDto.getId()).isEqualTo(1L);
        assertThat(commentDto.getText()).isEqualTo("comment_text");
    }

}
