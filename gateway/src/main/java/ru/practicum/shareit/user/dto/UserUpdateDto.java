package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.OnCreate;
import ru.practicum.shareit.user.OnUpdate;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @Email(message = "электронная почта должна содержать символ @", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    private String name;
}
