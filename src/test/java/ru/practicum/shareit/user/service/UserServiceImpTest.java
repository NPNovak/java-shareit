package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.error.exception.ConflictException;
import ru.practicum.shareit.error.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImpTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserServiceImp service;

    @Test
    void getUser_whenUserExist() {
        User user = new User();
        UserResponseDto userResponse = new UserResponseDto();

        when(repository.findById(anyInt())).thenReturn(Optional.of(user));
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);

        assertEquals(userResponse, service.getUserById(1));
    }

    @Test
    void getUser_whenUserNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getUserById(1));
    }

    @Test
    void getAllUsers_whenUsersExist() {
        User user = new User();
        UserResponseDto userResponse = new UserResponseDto();

        when(repository.findAll()).thenReturn(List.of(user, user, user));
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);

        List<UserResponseDto> usersResponse = new ArrayList(service.getAllUsers());

        assertEquals(3, usersResponse.size());
        assertEquals(userResponse, usersResponse.get(0));
        assertEquals(userResponse, usersResponse.get(1));
        assertEquals(userResponse, usersResponse.get(2));
    }

    @Test
    void getAllUsers_whenUsersNotFound() {
        when(repository.findAll()).thenReturn(List.of());

        assertEquals(0, service.getAllUsers().size());
    }

    @Test
    void addUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        User user = new User();
        UserResponseDto userResponse = new UserResponseDto();

        when(mapper.toUser(userCreateDto)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);

        BindingResult result = mock(BindingResult.class);

        assertEquals(userResponse, service.addUser(userCreateDto, result));
    }

    @Test
    void updateUser_whenUserNotFound() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();

        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        BindingResult result = mock(BindingResult.class);

        assertThrows(NotFoundException.class, () -> service.updateUser(userUpdateDto, 1, result));
    }

    @Test
    void updateUser_whenUserUpdateName() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("name");

        User userOld = new User();
        userOld.setName("name old");
        userOld.setEmail("email old");

        User user = new User();
        user.setName("name");

        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setName("name");
        userResponse.setEmail("email old");

        when(repository.findById(anyInt())).thenReturn(Optional.of(user));
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);
        when(mapper.toUser(userResponse)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);

        BindingResult result = mock(BindingResult.class);

        assertEquals(userResponse, service.updateUser(userUpdateDto, 1, result));
    }

    @Test
    void updateUser_whenUserUpdateEmail() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("email");

        User userOld = new User();
        userOld.setName("name old");
        userOld.setEmail("email old");

        User user = new User();
        user.setEmail("email");

        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setName("name old");
        userResponse.setEmail("email");

        when(repository.findById(anyInt())).thenReturn(Optional.of(user));
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);
        when(mapper.toUser(userResponse)).thenReturn(user);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);

        BindingResult result = mock(BindingResult.class);

        assertEquals(userResponse, service.updateUser(userUpdateDto, 1, result));
    }

    @Test
    void deleteUser_whenUserNotFound() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.deleteUserById(1));
    }

    @Test
    void deleteUser_whenUserExist() {
        User user = new User();
        UserResponseDto userResponse = new UserResponseDto();
        when(repository.findById(anyInt())).thenReturn(Optional.of(user));
        when(mapper.toUserResponseDto(user)).thenReturn(userResponse);

        assertEquals(null, service.deleteUserById(1));
    }

    @Test
    void getUserById_whenUserDoesNotExist() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getUserById(1));
        verify(repository, times(1)).findById(anyInt());
        verify(mapper, times(0)).toUserResponseDto(any());
    }

    @Test
    void checkEmail_whenEmailIsUnique() {
        when(repository.findByEmailAndIdNot(anyString(), anyInt())).thenReturn(null);

        service.checkEmail("example@example.com", 1);

        verify(repository, times(1)).findByEmailAndIdNot(anyString(), anyInt());
    }

    @Test
    void checkEmail_whenEmailIsDuplicate() {
        when(repository.findByEmailAndIdNot(anyString(), anyInt())).thenReturn(new User());

        assertThrows(ConflictException.class, () -> service.checkEmail("example@example.com", 1));

        verify(repository, times(1)).findByEmailAndIdNot(anyString(), anyInt());
    }
}