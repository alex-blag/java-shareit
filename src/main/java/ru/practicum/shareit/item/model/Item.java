package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Item {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long owner;

    private Long request;

    public Item(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.available = item.getAvailable();
        this.owner = item.getOwner();
        this.request = item.getRequest();
    }

}
