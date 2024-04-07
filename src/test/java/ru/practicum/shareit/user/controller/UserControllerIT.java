package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.service.UserServiceImp;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserServiceImp service;

    @Test
    void getUser_whenAllOk() throws Exception {
        UserResponseDto userResponse = new UserResponseDto();

        when(service.getUserById(anyInt())).thenReturn(userResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", 1))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponse));
    }

    @Test
    void getAllUsers_whenAllOk() throws Exception {
        List<UserResponseDto> userResponses = List.of();

        when(service.getAllUsers()).thenReturn(userResponses);

        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponses));
    }

    @Test
    void addUser_whenAllOk() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("Alice");
        userCreateDto.setEmail("test@gmail.com");

        UserResponseDto userResponseDto = new UserResponseDto();
        when(service.addUser(any(UserCreateDto.class), any(BindingResult.class))).thenReturn(userResponseDto);

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponseDto));
    }


    @Test
    @SneakyThrows
    void updateUser_whenAllOk() {
        Integer userId = 1;
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("Alice");
        userUpdateDto.setEmail("test@gmail.com");

        UserResponseDto userResponseDto = new UserResponseDto();

        when(service.updateUser(any(UserUpdateDto.class), anyInt(), any(BindingResult.class))).thenReturn(userResponseDto);

        String response = mockMvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponseDto));
    }

    @Test
    public void deleteUser_whenAllOk() throws Exception {
        Integer userId = 1;

        UserResponseDto userResponse = new UserResponseDto();

        when(service.deleteUserById(userId)).thenReturn(userResponse);

        String response = mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response, objectMapper.writeValueAsString(userResponse));
    }

}