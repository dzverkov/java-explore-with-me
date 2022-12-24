package ru.practicum.server.users.dto;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

    @NotNull
    private Long id;

    @NotBlank
    private String name;
}
