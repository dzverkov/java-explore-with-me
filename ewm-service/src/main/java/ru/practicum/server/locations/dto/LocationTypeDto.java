package ru.practicum.server.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationTypeDto {
    private Long id;

    @NotBlank
    private String name;
}
