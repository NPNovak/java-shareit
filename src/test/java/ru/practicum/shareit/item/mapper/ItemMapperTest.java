package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.dto.RequestedItemRequest;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ItemMapperImpl.class)
public class ItemMapperTest {

    @InjectMocks
    private ItemMapperImpl itemMapper;

    @Test
    public void testToItem() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("test");
        itemDto.setDescription("test");
        itemDto.setAvailable(true);

        Item item = new Item();
        item.setId(1);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        assertEquals(item, itemMapper.toItem(itemDto));
    }

    @Test
    public void testToItemNull() {
        assertNull(itemMapper.toItem((ItemDto) null));
    }

    @Test
    public void testUpdateToItem() {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setId(1);
        itemUpdateDto.setName("test");
        itemUpdateDto.setDescription("test");
        itemUpdateDto.setAvailable(true);

        Item item = new Item();
        item.setId(1);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        assertEquals(item, itemMapper.toItem(itemUpdateDto));
    }

    @Test
    public void testUpdateToItemNull() {
        assertNull(itemMapper.toItem((ItemUpdateDto) null));
    }

    @Test
    public void testToItemDto() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1);
        itemDto.setName("test");
        itemDto.setDescription("test");
        itemDto.setAvailable(true);

        Item item = new Item();
        item.setId(1);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        assertEquals(itemDto, itemMapper.toItemDto(item));
    }

    @Test
    public void testToItemDtoNull() {
        assertNull(itemMapper.toItemDto((Item) null));
    }

    @Test
    public void testToItemUpdateDto() {
        Item item = new Item();
        item.setId(1);
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setId(1);
        itemUpdateDto.setName("test");
        itemUpdateDto.setDescription("test");
        itemUpdateDto.setAvailable(true);

        assertEquals(itemUpdateDto, itemMapper.toItemUpdate(item));
    }

    @Test
    public void testToItemUpdateDtoNull() {
        assertNull(itemMapper.toItemUpdate((Item) null));
    }

    @Test
    public void testResponseToItem() {
        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setName("test");
        itemResponseDto.setDescription("test");
        itemResponseDto.setAvailable(true);

        Item item = new Item();
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        assertEquals(item, itemMapper.toItem(itemResponseDto));
    }

    @Test
    public void testResponseToItemNull() {
        assertNull(itemMapper.toItem((ItemResponseDto) null));
    }

    @Test
    public void testToItemResponseDto() {
        Item item = new Item();
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setName("test");
        itemResponseDto.setDescription("test");
        itemResponseDto.setAvailable(true);

        assertEquals(itemResponseDto, itemMapper.toItemResponseDto(item));
    }

    @Test
    public void testToItemResponseDtoNull() {
        assertNull(itemMapper.toItemResponseDto((Item) null));
    }

    @Test
    public void testToRequestedItemRequest() {
        Item item = new Item();
        item.setName("test");
        item.setDescription("test");
        item.setAvailable(true);

        RequestedItemRequest requestedItemRequest = new RequestedItemRequest();
        requestedItemRequest.setName("test");
        requestedItemRequest.setDescription("test");
        requestedItemRequest.setAvailable(true);
        requestedItemRequest.setRequestId(1);

        assertEquals(requestedItemRequest, itemMapper.toRequestedItemRequest(item, 1));
    }

    @Test
    public void testToRequestedItemRequestNull() {
        assertNull(itemMapper.toRequestedItemRequest((Item) null, null));
    }

    @Test
    void testToRequestedItemRequest_NullItemAndNullRequestId_ReturnsNull() {
        RequestedItemRequest requestedItemRequest = new RequestedItemRequest();
        requestedItemRequest.setRequestId(1);

        assertEquals(requestedItemRequest, itemMapper.toRequestedItemRequest(null, 1));
    }

    @Test
    public void testToUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setName("test");

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(1);
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("test");

        assertEquals(user, itemMapper.userCreateDtoToUser(userCreateDto));
    }

    @Test
    public void testToUserNull() {
        assertNull(itemMapper.userCreateDtoToUser((UserCreateDto) null));
    }

    @Test
    public void testToUserCreateDto() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(1);
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("test");

        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setName("test");

        assertEquals(userCreateDto, itemMapper.userToUserCreateDto(user));
    }

    @Test
    public void testToUserCreateDtoIdNull() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("test");

        User user = new User();
        user.setEmail("test@gmail.com");
        user.setName("test");

        assertEquals(userCreateDto, itemMapper.userToUserCreateDto(user));
    }

    @Test
    public void testToUserCreateDtoNull() {
        assertNull(itemMapper.userToUserCreateDto((User) null));
    }
}
