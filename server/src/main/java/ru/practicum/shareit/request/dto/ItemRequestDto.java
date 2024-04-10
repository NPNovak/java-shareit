package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.RequestedItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemRequestDto {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private List<RequestedItemRequest> items;
}
