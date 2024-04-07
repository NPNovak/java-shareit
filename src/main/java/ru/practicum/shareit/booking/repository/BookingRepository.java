package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerId(Integer bookerID, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(Integer bookerID, LocalDateTime start, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBefore(Integer bookerID, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfter(Integer bookerID, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByBookerIdAndStatus(Integer bookerID, String status, Pageable pageable);

    List<Booking> findAllByItemOwner(User itemOwner, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(User itemOwner, LocalDateTime start, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByItemOwnerAndEndBefore(User itemOwner, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStartAfter(User itemOwner, LocalDateTime before, Pageable pageable);

    List<Booking> findAllByItemOwnerAndStatus(User itemOwner, String status, Pageable pageable);

    Booking findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByStartDesc(Integer itemId, Integer bookerID, LocalDateTime end, String status);

    Booking findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(Integer itemId, Integer bookerID, LocalDateTime start, String status);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBefore(Integer itemId, Integer bookerID, LocalDateTime end);
}
