package ru.practicum.shareit.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Long id;

    private String description;

    private UserDto requester;

    private LocalDateTime created;

    private List<ItemDto> items;

}
