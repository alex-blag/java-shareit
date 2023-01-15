package ru.practicum.shareit.gateway.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchDto {

    private String name;

    @Email
    private String email;

}
