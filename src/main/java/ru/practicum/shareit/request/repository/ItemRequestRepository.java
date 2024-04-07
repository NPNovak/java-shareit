package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;


public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findByRequesterIdOrderByCreatedDesc(Integer requestorId);

    List<ItemRequest> findByRequesterIdNot(Integer userId, Pageable pageable);
}
