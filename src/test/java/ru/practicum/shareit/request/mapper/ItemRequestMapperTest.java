package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.RequestedItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemRequest;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ItemRequest.class)
public class ItemRequestMapperTest {
    @InjectMocks
    private ItemRequestMapperImpl itemRequestMapper;

    @Test
    public void testToItemRequest() {
        RequestItemRequest requestItemRequest = new RequestItemRequest();
        requestItemRequest.setDescription("test");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription("test");

        assertEquals(itemRequest, itemRequestMapper.toItemRequest(requestItemRequest));
    }

    @Test
    public void testToItemRequestNull() {
        assertNull(itemRequestMapper.toItemRequest((RequestItemRequest) null));
    }

    @Test
    public void testToItemRequestDto() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("test");


        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("test");

        assertEquals(itemRequestDto, itemRequestMapper.toItemRequestDto(itemRequest));
    }

    @Test
    public void testToItemRequestDtoNull() {
        assertNull(itemRequestMapper.toItemRequestDto((ItemRequest) null));
    }

    @Test
    public void testToItemRequestDtoAndItems() {
        List<RequestedItemRequest> list = List.of(new RequestedItemRequest());

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("test");
        itemRequestDto.setItems(list);


        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("test");

        assertEquals(itemRequestDto, itemRequestMapper.toItemRequestDto(itemRequest, list));
    }

    @Test
    public void testToItemRequestDtoAndItemsAndListNull() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1);
        itemRequestDto.setDescription("test");

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1);
        itemRequest.setDescription("test");

        assertEquals(itemRequestDto, itemRequestMapper.toItemRequestDto(itemRequest, null));
    }

    @Test
    void testToItemRequestDtoAndItemsItemRequestNull() {
        assertNull(itemRequestMapper.toItemRequestDto(null, null));
    }
}
