package ru.practicum.server.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.locations.model.LocationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLocationDto {
    private Long id;

    private String name;

    private LocationType locationType;

    private Float lat;

    private Float lon;

    private Float  radius;
}
