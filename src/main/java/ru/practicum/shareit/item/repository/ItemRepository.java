package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByOwnerId(Integer id, Pageable pageable);

    Optional<Item> findFirstByOwnerId(long userId);

    List<Item> findByRequestIdOrderByRequestCreatedDesc(Integer requestId);

    @Query(value = "SELECT * FROM items AS i " +
            "WHERE i.is_available = TRUE " +
            "AND (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')))",
            nativeQuery = true)
    List<Item> search(@Param("text") String text, Pageable pageable);

    void deleteByOwnerId(long ownerId);
}
