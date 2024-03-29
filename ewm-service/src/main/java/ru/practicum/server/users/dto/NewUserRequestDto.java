package ru.practicum.server.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String name;
}
