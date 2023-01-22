package ru.practicum.server.locations.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.server.events.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class EventFilterParamsWithLocation {
    private Long locationId;
    private List<EventState> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
}
