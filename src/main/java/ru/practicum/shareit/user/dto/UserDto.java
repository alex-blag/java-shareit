package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.common.validation.Patch;
import ru.practicum.shareit.common.validation.Post;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(groups = Post.class)
    private String name;

    @NotBlank(groups = Post.class)
    @Email(groups = {Post.class, Patch.class})
    private String email;

}
