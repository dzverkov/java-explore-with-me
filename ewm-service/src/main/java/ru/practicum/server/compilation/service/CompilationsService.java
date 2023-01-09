package ru.practicum.server.compilation.service;

import ru.practicum.server.compilation.model.Compilation;

import java.util.List;

public interface CompilationsService {
    List<Compilation> getCompilationsPublic(Boolean pinned, Integer from, Integer size);

    Compilation getCompilationByIdPublic(Long compId);

    Compilation addCompilationAdmin(Compilation newCompilation);

    void deleteCompilationAdmin(Long compId);

    void deleteEventFromCompilationAdmin(Long compId, Long eventId);

    void addEventToCompilationAdmin(Long compId, Long eventId);

    void unpinCompilationAdmin(Long compId);

    void pinCompilationAdmin(Long compId);
}
