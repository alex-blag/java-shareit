package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.common.validation.Post;

import javax.validation.constraints.NotBlank;

@Data
public class RequestPostDto {

    @NotBlank(groups = Post.class)
    private String description;

}
