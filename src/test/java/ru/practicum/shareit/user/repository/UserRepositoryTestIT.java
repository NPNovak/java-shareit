package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.entity.User;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(showSql = true)
class UserRepositoryTestIT {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void findByEmailAndIdNot() {
        User user = new User(1, "Alice", "test@gmail.com");
        ;
        userRepository.save(user);

        User result = userRepository.findByEmailAndIdNot("test@gmail.com", 2);
        assertEquals(result, null);
    }
}