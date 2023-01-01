package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.common.TestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentPostDtoJsonTest {

    @Autowired
    JacksonTester<CommentPostDto> json;

    @Test
    void testSerialize() throws Exception {
        CommentPostDto dto = TestUtils.getComment1PostDto();

        JsonContent<CommentPostDto> content = json.write(dto);

        assertThat(content).hasJsonPathStringValue("$.text");

        assertThat(content).doesNotHaveJsonPath("$.id");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\": 1, \"text\": \"comment_text\"}";

        CommentPostDto dto = json.parse(content).getObject();

        assertThat(dto.getText()).isEqualTo("comment_text");
    }

}
