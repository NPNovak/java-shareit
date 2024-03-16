package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.error.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.util.ItemValidation;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImp implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper mapper;

    @Transactional
    public ItemResponseDto addItem(Integer userId, ItemDto itemDto, BindingResult bindingResult) throws ValidationException {
        ItemValidation.validation(bindingResult);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        Item item = mapper.toItem(itemDto);
        item.setOwner(user);
        log.info("Добавлен товар");
        return mapper.toItemResponseDto(itemRepository.save(item));
    }

    @Transactional
    public ItemResponseDto updateItem(Integer userId, Integer itemId, ItemUpdateDto itemUpdate) {
        Item inItem = mapper.toItem(getItemById(itemId, userId));
        if (inItem.getOwner().getId() == userId) {
            if (itemUpdate.getName() != null) {
                inItem.setName(itemUpdate.getName());
            }
            if (itemUpdate.getDescription() != null) {
                inItem.setDescription(itemUpdate.getDescription());
            }
            if (itemUpdate.getAvailable() != null) {
                inItem.setAvailable(itemUpdate.getAvailable());
            }
            itemRepository.save(inItem);
            log.info("Обновлен товар");
            return mapper.toItemResponseDto(inItem);
        } else {
            throw new NotFoundException("Данный товар не принадлежит пользователю с id = " + userId);
        }
    }

    public ItemResponseDto getItemById(Integer itemId, Integer userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Товара с такими id нет"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        ItemResponseDto itemResponse = mapper.toItemResponseDto(item);
        if (user.getId() == item.getOwner().getId()) {
            addLastAndNextBooking(itemResponse, userId);
        }
        List<Comment> commentList = commentRepository.findByItemId(itemId);
        itemResponse.setComments(commentList.stream().map(comment -> {
            CommentResponseDto commentResponse = new CommentResponseDto();
            commentResponse.setId(comment.getId());
            commentResponse.setText(comment.getText());
            commentResponse.setAuthorName(comment.getAuthor().getName());
            commentResponse.setCreated(comment.getCreated());
            return commentResponse;
        }).collect(Collectors.toList()));
        log.info("Отображен товар с id = {}", itemId);
        return itemResponse;
    }

    public Collection<ItemResponseDto> getAllItems(Integer userId) {
        log.info("Отображен список всех товаров пользователя");
        return itemRepository.findByOwnerId(userId).stream().map(x -> getItemById(x.getId(), userId)).collect(Collectors.toList());
    }

    public Collection<ItemResponseDto> search(String text) {
        log.info("Вывод результатов поиска");
        if (text.isEmpty()) {
            return Collections.emptyList();
        }

        return itemRepository.findAll().stream()
                .filter(x -> x.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))
                        || x.getDescription().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)))
                .filter(Item::getAvailable)
                .map(mapper::toItemResponseDto)
                .collect(Collectors.toList());
    }

    public CommentResponseDto addComment(Integer userId, CommentDto commentDto, Integer itemId) throws ValidationException {
        if (commentDto.getText().isBlank() || commentDto.getText() == null) {
            throw new ValidationException("поле comment не может быть пустым");
        }
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Товара с такими id нет"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователя с такими id нет"));
        bookingRepository.findFirstByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("User с заданным id = " + userId + " ещё не брал в аренду этот предмет"));
        if (item.getOwner().getId() == userId) {
            throw new ValidationException("User с заданным id = " + userId + " является владельцем");
        }
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        log.info("Отзыв добавлен");
        return new CommentResponseDto(comment.getId(), comment.getText(), comment.getAuthor().getName(), comment.getCreated());
    }

    public void addLastAndNextBooking(ItemResponseDto item, Integer ownerId) {
        Booking lastBooking = bookingRepository
                .findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        "APPROVED");
        Booking nextBooking = bookingRepository
                .findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(
                        item.getId(),
                        ownerId,
                        LocalDateTime.now(),
                        "APPROVED");

        if (lastBooking != null) {
            item.setLastBooking(new ItemBookingDto(lastBooking.getId(), lastBooking.getBooker().getId()));
        }
        if (nextBooking != null) {
            item.setNextBooking(new ItemBookingDto(nextBooking.getId(), nextBooking.getBooker().getId()));
        }
    }
}
