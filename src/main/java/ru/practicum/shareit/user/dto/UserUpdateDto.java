package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @Email(message = "электронная почта должна содержать символ @")
    private String email;

    private String name;
}
