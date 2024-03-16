package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.entity.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentResponseDto toCommentResponseDto(Comment comment);

}
