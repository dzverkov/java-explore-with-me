package ru.practicum.server.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.server.events.dto.EventShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    @NotNull
    private Long id;

    private List<EventShortDto> events;

    @NotNull
    private Boolean pinned;

    @NotBlank
    private  String title;
}
