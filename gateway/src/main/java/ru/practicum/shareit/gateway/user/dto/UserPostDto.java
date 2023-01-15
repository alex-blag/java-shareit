package ru.practicum.shareit.gateway.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostDto {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

}
