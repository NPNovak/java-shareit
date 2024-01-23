package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user) throws Exception;

    User updateUser(User user, Integer userId) throws Exception;

    User getUser(Integer userId);

    User deleteUser(Integer userId);

    Collection<User> getAllUsers();
}
