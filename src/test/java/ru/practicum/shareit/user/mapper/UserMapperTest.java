package ru.practicum.shareit.user.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.entity.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserMapperImpl.class)
public class UserMapperTest {
    @InjectMocks
    private UserMapperImpl userMapper;

    @Test
    public void testToUser() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(1);
        userCreateDto.setName("test");
        userCreateDto.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(user, userMapper.toUser(userCreateDto));
    }

    @Test
    public void testToUserNull() {
        assertNull(userMapper.toUser((UserCreateDto) null));
    }

    @Test
    public void testToUserCreateDto() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(1);
        userCreateDto.setName("test");
        userCreateDto.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(userCreateDto, userMapper.toUserCreateDto(user));
    }

    @Test
    public void testToUserCreateDtoIdNull() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("test");
        userCreateDto.setEmail("test@gmail.com");

        User user = new User();
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(userCreateDto, userMapper.toUserCreateDto(user));
    }

    @Test
    public void testToUserCreateDtoNull() {
        assertNull(userMapper.toUserCreateDto((User) null));
    }

    @Test
    public void testUpdateToUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setName("test");
        userUpdateDto.setEmail("test@gmail.com");

        User user = new User();
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(user, userMapper.toUser(userUpdateDto));
    }

    @Test
    public void testUpdateToUserNull() {
        assertNull(userMapper.toUser((UserUpdateDto) null));
    }

    @Test
    public void testResponseToUser() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1);
        userResponseDto.setName("test");
        userResponseDto.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(user, userMapper.toUser(userResponseDto));
    }

    @Test
    public void testResponseToUserNull() {
        assertNull(userMapper.toUser((UserResponseDto) null));
    }

    @Test
    public void testUserToResponse() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1);
        userResponseDto.setName("test");
        userResponseDto.setEmail("test@gmail.com");

        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(userResponseDto, userMapper.toUserResponseDto(user));
    }

    @Test
    public void testUserToResponseIdNull() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName("test");
        userResponseDto.setEmail("test@gmail.com");

        User user = new User();
        user.setName("test");
        user.setEmail("test@gmail.com");

        assertEquals(userResponseDto, userMapper.toUserResponseDto(user));
    }

    @Test
    public void testUserToResponseNull() {
        assertNull(userMapper.toUserResponseDto((User) null));
    }

    @Test
    public void testUserCreateDto() {
        User user = new User();
        user.setId(1);
        user.setEmail("test@gmail.com");
        user.setName("test");

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setId(1);
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("test");

        assertEquals(userCreateDto, userMapper.toUserCreateDto(user));
    }

    @Test
    public void testUpdateToUserCreateDto() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setName("test");

        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@gmail.com");
        userCreateDto.setName("test");

        assertEquals(userCreateDto, userMapper.toUserUpdateDto(user));
    }

    @Test
    public void testUpdateToUserCreateDtoNull() {
        assertNull(userMapper.toUserUpdateDto((User) null));
    }
}
