package ru.practicum.shareit.item.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    private Item item1;
    private Item item2;
    private User user1;
    private User user2;
    private User user3;

    private User user4;

    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;


    @BeforeEach
    @Transactional
    void setUp() {
        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1;").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE items ALTER COLUMN id RESTART WITH 1;").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE requests ALTER COLUMN id RESTART WITH 1;").executeUpdate();

        user1 = new User(1,"test@gmail.com","alice1");

        userRepository.save(user1);
        user2 = new User(2,"test2@gmail.com", "alice2");
        userRepository.save(user2);

        user3 = new User(3,"test3@gmail.com", "alice3");
        userRepository.save(user3);

        user4 = new User(4,"test4@gmail.com", "alice4");
        userRepository.save(user4);

        item1 = new Item();
        item1.setName("Item 1");
        item1.setDescription("Description 1");
        item1.setAvailable(true);
        item1.setOwner(user1);

        item2 = new Item();
        item2.setName("Item 2");
        item2.setDescription("Description 2");
        item2.setAvailable(false);
        item2.setOwner(user2);

        itemRepository.saveAll(List.of(item1, item2));

        itemRequest1 = new ItemRequest();
        itemRequest1.setCreated(LocalDateTime.now().minusDays(1));
        itemRequest1.setRequester(user1);
        itemRequest1.setDescription("description 1");

        itemRequest2 = new ItemRequest();
        itemRequest2.setDescription("description 2");
        itemRequest2.setRequester(user2);
        itemRequest2.setCreated(LocalDateTime.now());

        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);

        item1 = new Item();
        item1.setName("Item 3");
        item1.setDescription("Description 3");
        item1.setAvailable(true);
        item1.setOwner(user3);
        item1.setRequest(itemRequest1);

        item2 = new Item();
        item2.setName("Item 4");
        item2.setDescription("Description 4");
        item2.setAvailable(true);
        item2.setOwner(user4);
        item2.setRequest(itemRequest2);

        itemRepository.saveAll(List.of(item1, item2));
    }

    @Test
    void findByOwnerId() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Item> items = itemRepository.findByOwnerId(item1.getOwner().getId(), pageable);

        assertThat(items).hasSize(1);
        assertThat(items.get(0)).isEqualTo(item1);
    }

    @Test
    void findFirstByOwnerId() {
        Optional<Item> item = itemRepository.findFirstByOwnerId(item1.getOwner().getId());

        assertThat(item).isPresent();
        assertThat(item.get()).isEqualTo(item1);
    }

    @Test
    void findByRequestIdOrderByRequestCreatedDesc() {
        List<Item> items = itemRepository.findByRequestIdOrderByRequestCreatedDesc(itemRequest1.getId());

        assertThat(items).hasSize(1);
        assertThat(items.get(0)).isEqualTo(item1);
    }

    @Test
    void search() {
        String text = "Item";
        Pageable pageable = PageRequest.of(0, 10);
        List<Item> items = itemRepository.search(text, pageable);

        assertThat(items).hasSize(3);
        assertThat(items).contains(item1, item2);
    }

    @Test
    void deleteByOwnerId() {
        itemRepository.deleteByOwnerId(item1.getOwner().getId());

        Optional<Item> item = itemRepository.findById(item1.getId());

        assertThat(item).isEmpty();
    }
}