package ru.practicum.server.events.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequestDto {

    @Size(min = 20, max = 2000, message = "Размер поля annotation должна быть в диапазоне от 20 до 2000")
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, message = "Размер поля description должна быть в диапазоне от 20 до 7000")
    private String description;

    private LocalDateTime eventDate;

    @NotNull
    private Long eventId;

    private Boolean paid;

    private Integer participantLimit;

    @Size(min = 3, max = 120, message = "Размер поля title должна быть в диапазоне от 3 до 120")
    private String title;
}
