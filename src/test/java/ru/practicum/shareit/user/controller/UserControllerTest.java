package ru.practicum.shareit.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserServiceImp;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserServiceImp userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetUserById() {
        UserResponseDto mockUserResponseDto = new UserResponseDto();
        mockUserResponseDto.setId(1);
        mockUserResponseDto.setName("John Doe");

        when(userService.getUserById(anyInt())).thenReturn(mockUserResponseDto);

        ResponseEntity<UserResponseDto> responseEntity = userController.getUser(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUserResponseDto, responseEntity.getBody());
    }

    @Test
    void testGetAllUsers() {
        UserResponseDto user1 = new UserResponseDto(1, "test@gmail.com", "Alice");
        UserResponseDto user2 = new UserResponseDto(2, "test2@gmail.com","Bob");
        List<UserResponseDto> mockUsers = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(mockUsers);

        ResponseEntity<Collection<UserResponseDto>> responseEntity = userController.getAllUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUsers, responseEntity.getBody());
    }

    @Test
    void testAddUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto(1, "test@gmail.com","Alice");

        UserResponseDto mockUserResponseDto = new UserResponseDto(1, "test@gmail.com", "Alice");
        when(userService.addUser(userCreateDto, null)).thenReturn(mockUserResponseDto);

        ResponseEntity<UserResponseDto> responseEntity = userController.addUser(userCreateDto, null);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockUserResponseDto, responseEntity.getBody());
    }

    @Test
    void testUpdateUser() throws Exception {
        UserUpdateDto userUpdateDto = new UserUpdateDto("test@gmail.com","Alice");

        UserResponseDto mockUserResponseDto = new UserResponseDto(1, "test@gmail.com","Alice");
        when(userService.updateUser(userUpdateDto, 1, null)).thenReturn(mockUserResponseDto);

        ResponseEntity<UserResponseDto> responseEntity = userController.updateUser(userUpdateDto, 1, null);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUserResponseDto, responseEntity.getBody());
    }

    @Test
    void testDeleteUser() {
        Integer userId = 1;

        UserResponseDto mockUserResponseDto = new UserResponseDto(1, "test@gmail.com","Alice");
        when(userService.deleteUserById(userId)).thenReturn(mockUserResponseDto);

        ResponseEntity<UserResponseDto> responseEntity = userController.deleteUser(userId);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockUserResponseDto, responseEntity.getBody());
    }
}