package ru.practicum.shareit.item.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceImpTest {

    @InjectMocks
    private ItemServiceImp itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemMapper mapper;

    @Test
    void getItemByIdWithNotFoundException() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        try {
            itemService.getItemById(1, 1);
        } catch (NotFoundException e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
        }

        verify(itemRepository, times(1)).findById(any());
        verify(userRepository, times(0)).findById(any());
        verify(mapper, times(0)).toItemResponseDto(any());
    }

    @Test
    void updateItemWithNotFoundException() {
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Item 1 updated");
        itemUpdateDto.setDescription("Description 1 updated");
        itemUpdateDto.setAvailable(false);

        try {
            itemService.updateItem(1, 1, itemUpdateDto);
        } catch (NotFoundException e) {
            assertThat(e).isInstanceOf(NotFoundException.class);
        }

        verify(itemRepository, times(0)).save(any());
        verify(mapper, times(0)).toItemResponseDto(any());
    }

    @Test
    void getItem_whenAllOk() {
        User owner = new User();
        owner.setId(1);

        Item item = new Item();
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2);

        Booking lastBooking = new Booking(5, LocalDateTime.now(), LocalDateTime.now(), null, booker, "APPROVED");
        Booking nextBooking = new Booking(6, LocalDateTime.now(), LocalDateTime.now(), null, booker, "APPROVED");

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setLastBooking(new ItemBookingDto(1, 5));
        itemResponseDto.setLastBooking(new ItemBookingDto(2, 6));

        List<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Comment comment = new Comment();
            comment.setId(i + 1);
            comment.setText("comment" + i);
            comment.setAuthor(new User());
            comment.setCreated(LocalDateTime.now());
            commentList.add(comment);
        }

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(owner));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(mapper.toItemResponseDto(item)).thenReturn(itemResponseDto);
        when(commentRepository.findByItemId(anyInt())).thenReturn(commentList);
        when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(anyInt(), anyInt(), any(LocalDateTime.class), anyString()))
                .thenReturn(lastBooking);
        when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(anyInt(), anyInt(), any(LocalDateTime.class), anyString()))
                .thenReturn(nextBooking);

        ItemResponseDto result = itemService.getItemById(1, 1);

        assertNotNull(itemResponseDto.getComments());
        assertEquals(commentList.size(), itemResponseDto.getComments().size());
        assertEquals(commentList.get(0).getId(), itemResponseDto.getComments().get(0).getId());
        assertEquals(commentList.get(0).getText(), itemResponseDto.getComments().get(0).getText());
        assertEquals(commentList.get(0).getAuthor().getName(), itemResponseDto.getComments().get(0).getAuthorName());
        assertEquals(commentList.get(0).getCreated(), itemResponseDto.getComments().get(0).getCreated());
        assertEquals(itemResponseDto, result);
    }

    @Test
    void searchItems_whenAllOk() {
        Item item = new Item();
        ItemResponseDto itemResponseDto = new ItemResponseDto();

        when(itemRepository.search(anyString(), any(Pageable.class))).thenReturn(List.of(item, item, item));
        when(mapper.toItemResponseDto(item)).thenReturn(itemResponseDto);

        List<ItemResponseDto> itemResponseDtos = new ArrayList(itemService.search("text", 1, 1));

        assertEquals(3, itemResponseDtos.size());
        assertEquals(itemResponseDto, itemResponseDtos.get(0));
        assertEquals(itemResponseDto, itemResponseDtos.get(1));
        assertEquals(itemResponseDto, itemResponseDtos.get(2));
    }

    @Test
    void addItem_whenAllOk() {
        User user = new User();

        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(1);

        Item item = new Item();

        ItemResponseDto itemResponseDto = new ItemResponseDto();

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(mapper.toItem(itemDto)).thenReturn(item);
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(new ItemRequest()));
        when(itemRepository.save(item)).thenReturn(item);
        when(mapper.toItemResponseDto(item)).thenReturn(itemResponseDto);

        BindingResult result = mock(BindingResult.class);

        assertEquals(itemResponseDto, itemService.addItem(1, itemDto, result));
    }

    @Test
    void addItem_whenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        BindingResult result = mock(BindingResult.class);

        assertThrows(NotFoundException.class, () -> itemService.addItem(1, new ItemDto(), result));
    }

    @Test
    void addItem_whenItemRequestNotFound() {
        ItemDto itemDto = new ItemDto();
        itemDto.setRequestId(1);

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(mapper.toItem(itemDto)).thenReturn(new Item());
        when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.empty());

        BindingResult result = mock(BindingResult.class);

        assertThrows(NotFoundException.class, () -> itemService.addItem(1, itemDto, result));
    }

    @Test
    void addComment_whenAllOk() {
        User owner = new User();
        Item item = new Item();
        owner.setId(1);
        item.setOwner(owner);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("test");
        Comment comment = new Comment();
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setText("test");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(anyInt(), anyInt(), any(LocalDateTime.class))).thenReturn(Optional.of(new Booking()));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        BindingResult result = mock(BindingResult.class);

        CommentResponseDto resultResponse = itemService.addComment(2, commentDto, 2);

        commentResponseDto.setCreated(resultResponse.getCreated());

        assertEquals(commentResponseDto, resultResponse);
    }

    @Test
    void addComment_whenUserNotFound() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("test");

        assertThrows(NotFoundException.class, () -> itemService.addComment(1, commentDto, 1));
    }

    @Test
    void addComment_whenItemNotFound() {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("test");

        //when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        //when(itemRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.addComment(1, commentDto, 1));
    }

    @Test
    void addComment_whenOwnerAddComment() {
        User owner = new User();
        Item item = new Item();
        owner.setId(1);
        item.setOwner(owner);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("test");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));

        assertThrows(ValidationException.class, () -> itemService.addComment(1, commentDto, 1));
    }

    @Test
    void addComment_whenBookerNotFound() {
        User owner = new User();
        Item item = new Item();
        owner.setId(1);
        item.setOwner(owner);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("test");

        when(userRepository.findById(anyInt())).thenReturn(Optional.of(new User()));
        when(itemRepository.findById(anyInt())).thenReturn(Optional.of(item));
        when(bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(anyInt(), anyInt(), any(LocalDateTime.class))).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> itemService.addComment(2, commentDto, 1));
    }

    @Test
    void updateItem_WhenItemFound() {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(2);
        itemDto.setRequestId(1);

        Item itemOld = new Item();
        itemOld.setId(1);
        itemOld.setName("name old");
        itemOld.setDescription("description old");
        itemOld.setAvailable(true);

        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setId(3);
        itemUpdateDto.setName("name new");
        itemUpdateDto.setDescription("description new");
        itemUpdateDto.setAvailable(false);
        itemUpdateDto.setRequestId(2);

        ItemResponseDto itemResponseDto = new ItemResponseDto();
        itemResponseDto.setId(1);
        itemResponseDto.setName("name new");
        itemResponseDto.setDescription("description new");
        itemResponseDto.setAvailable(false);
        itemResponseDto.setRequestId(2);
        itemResponseDto.setComments(List.of());

        User owner = new User();
        owner.setId(1);
        itemOld.setOwner(owner);

        User booker = new User();
        booker.setId(2);


        when(itemRepository.findById(1)).thenReturn(Optional.of(itemOld));
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(mapper.toItemResponseDto(itemOld)).thenReturn(itemResponseDto);
        when(commentRepository.findByItemId(anyInt())).thenReturn(List.of());
        when(mapper.toItem(itemResponseDto)).thenReturn(itemOld);
        when(itemRequestRepository.findById(2)).thenReturn(Optional.of(new ItemRequest()));
        when(itemRepository.save(itemOld)).thenReturn(itemOld);
        when(mapper.toItemResponseDto(itemOld)).thenReturn(itemResponseDto);

        assertEquals(itemResponseDto, itemService.updateItem(1, 1, itemUpdateDto));
    }

    @Test
    void getAllItems_whenAllOk() {
        User owner = new User();
        owner.setId(1);

        Item item1 = new Item();
        item1.setId(1);
        item1.setName("item1");
        item1.setDescription("description1");
        item1.setAvailable(true);
        item1.setOwner(owner);

        Item item2 = new Item();
        item2.setId(2);
        item2.setName("item2");
        item2.setDescription("description2");
        item2.setAvailable(false);
        item2.setOwner(owner);

        ItemResponseDto itemResponseDto1 = new ItemResponseDto();
        itemResponseDto1.setId(1);
        itemResponseDto1.setName("item1");
        itemResponseDto1.setDescription("description1");
        itemResponseDto1.setAvailable(true);
        itemResponseDto1.setRequestId(2);
        itemResponseDto1.setComments(List.of());

        ItemResponseDto itemResponseDto2 = new ItemResponseDto();
        itemResponseDto2.setId(2);
        itemResponseDto2.setName("item2");
        itemResponseDto2.setDescription("description2");
        itemResponseDto2.setAvailable(false);
        itemResponseDto1.setRequestId(2);
        itemResponseDto1.setComments(List.of());

        when(itemRepository.findById(1)).thenReturn(Optional.of(item1));
        when(userRepository.findById(1)).thenReturn(Optional.of(owner));
        when(mapper.toItemResponseDto(item1)).thenReturn(itemResponseDto1);
        when(commentRepository.findByItemId(anyInt())).thenReturn(List.of());
        when(itemRepository.findById(2)).thenReturn(Optional.of(item2));
        when(commentRepository.findByItemId(anyInt())).thenReturn(List.of());
        when(mapper.toItemResponseDto(item2)).thenReturn(itemResponseDto2);
        when(itemRepository.findByOwnerId(1, PageRequest.of(0, 2))).thenReturn(Arrays.asList(item1, item2));

        assertEquals(Arrays.asList(itemResponseDto1, itemResponseDto2), itemService.getAllItems(1, 0, 2));
    }
}