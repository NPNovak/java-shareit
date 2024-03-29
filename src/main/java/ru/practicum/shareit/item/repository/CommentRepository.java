package ru.practicum.shareit.item.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByItemId(Integer itemId);
}
