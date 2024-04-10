package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RequestedItemRequest {
    private long id;
    private String name;
    private String description;
    private boolean available;
    private Integer requestId;
}
