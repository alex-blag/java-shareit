package ru.practicum.shareit.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPatchDto {

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

}
