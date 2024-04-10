package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.user.OnCreate;
import ru.practicum.shareit.user.OnUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserCreateDto {

    private int id;

    @NotBlank(message = "Email пустой или null", groups = OnCreate.class)
    @Email(message = "электронная почта должна содержать символ @", groups = {OnCreate.class, OnUpdate.class})
    @Size(max = 255, message = "Email больше 255 символов", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotBlank(message = "поле name не может быть пустым", groups = OnCreate.class)
    private String name;
}
