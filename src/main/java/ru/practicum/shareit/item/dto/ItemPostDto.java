package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.common.validation.Post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPostDto {

    @NotBlank(groups = Post.class)
    private String name;

    @NotBlank(groups = Post.class)
    private String description;

    @NotNull(groups = Post.class)
    private Boolean available;

    private Long requestId;

}
