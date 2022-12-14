package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.common.validation.Post;

import javax.validation.constraints.NotBlank;

@Data
public class CommentPostDto {

    @NotBlank(groups = Post.class)
    private String text;

}
