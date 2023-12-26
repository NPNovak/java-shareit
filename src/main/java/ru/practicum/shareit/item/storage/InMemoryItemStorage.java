package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    protected final Map<Integer, Item> itemMap = new HashMap<>();
    private int id = 0;

    @Override
    public Item addItem(Item item, User user) {
        log.info("Добавление товара");
        item.setId(++id);
        item.setOwner(user);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Integer userId, Integer itemId) {
        log.info("Обновление товара");
        getItem(itemId);
        Item thisItem = itemMap.get(itemId);
        if (thisItem.getOwner().getId() == userId) {
            if (item.getName() != null) {
                thisItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                thisItem.setDescription(item.getDescription());
            }
            if (item.getAvailable() != null) {
                thisItem.setAvailable(item.getAvailable());
            }
            itemMap.put(itemId, thisItem);
            return thisItem;
        } else {
            throw new NotFoundException("Товар не найден у пользователя = " + userId);
        }
    }

    @Override
    public Item getItem(Integer itemId) {
        if (itemMap.containsKey(itemId)) {
            log.info("Получение товава с id = {}", itemId);
            return itemMap.get(itemId);
        } else {
            throw new NotFoundException("Товара с такими id нет");
        }
    }

    @Override
    public Collection<Item> getAllItems(Integer userId) {
        log.info("Получение всех товаров пользователя");
        return itemMap.values().stream().filter(x -> x.getOwner().getId() == userId).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> search(String text) {
        log.info("Поиск товара");
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return itemMap.values().stream()
                .filter(x -> x.getName().toLowerCase().contains(text.toLowerCase())
                        || x.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
