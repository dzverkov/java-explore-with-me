package ru.practicum.server.compilation.service;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.compilation.exception.CompilationNotFoundException;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.QCompilation;
import ru.practicum.server.compilation.repository.CompilationRepository;
import ru.practicum.server.events.exception.EventNotFoundException;
import ru.practicum.server.events.model.Event;
import ru.practicum.server.events.repository.EventRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Override
    public List<Compilation> getCompilationsPublic(Boolean pinned, Integer from, Integer size) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        QCompilation compilation = QCompilation.compilation;
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (pinned != null) {
            booleanBuilder.and(compilation.pinned.eq(pinned));
        }
        Predicate predicate = booleanBuilder.getValue();
        if (predicate != null) {
            return compilationRepository.findAll(booleanBuilder.getValue(), pageable).getContent();
        } else {
            return compilationRepository.findAll(pageable).getContent();
        }
    }

    @Override
    public Compilation getCompilationByIdPublic(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
    }

    @Override
    public Compilation addCompilationAdmin(Compilation newCompilation) {

        List<Long> eventsIds = newCompilation.getEvents().stream().map(Event::getId).collect(Collectors.toList());

        List<Event> events = eventRepository.getEventsByIdIn(eventsIds);
        newCompilation.setEvents(events);
        return compilationRepository.save(newCompilation);
    }

    @Override
    public void deleteCompilationAdmin(Long compId) {

        compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventFromCompilationAdmin(Long compId, Long eventId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));

        List<Event> events = compilation.getEvents()
                .stream().filter(e -> !Objects.equals(e.getId(), eventId))
                .collect(Collectors.toList());
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilationAdmin(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void setPinnedCompilationAdmin(Long compId, boolean pinned) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException(compId));
        compilation.setPinned(pinned);
        compilationRepository.save(compilation);
    }
}
