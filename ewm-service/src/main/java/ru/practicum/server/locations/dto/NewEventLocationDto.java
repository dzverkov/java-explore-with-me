package ru.practicum.server.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventLocationDto {

    @NotBlank
    private String name;

    @NotNull
    private Long locationType;

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;

    @NotNull
    private Float  radius;

}
