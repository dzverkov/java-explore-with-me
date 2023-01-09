package ru.practicum.server.events.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.server.categories.exception.CategoryNotFoundException;
import ru.practicum.server.categories.model.Category;
import ru.practicum.server.categories.repository.CategoryRepository;
import ru.practicum.server.events.controller.EventMapper;
import ru.practicum.server.events.exception.EventNotFoundException;
import ru.practicum.server.events.model.*;
import ru.practicum.server.events.model.QEvent;
import ru.practicum.server.events.repository.EventRepository;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.request.exception.RequestNotFoundException;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.RequestStatus;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.stat.StatClient;
import ru.practicum.server.stat.dto.EndpointHitDto;
import ru.practicum.server.users.exception.UserNotFoundException;
import ru.practicum.server.users.model.User;
import ru.practicum.server.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final RequestRepository requestRepository;

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    private final StatClient statClient;

    @Override
    public List<Event> getAllEventsPublic(EventFilterParamsPublic eventFilterParams,
                                          Integer from, Integer size, HttpServletRequest request) {

        statClient.addHit(
                new EndpointHitDto(request.getServerName(), request.getRequestURI(),
                        request.getRemoteAddr(), LocalDateTime.now())
        );

        Sort sort = (eventFilterParams.getSort().equals("EVENT_DATE") ?
                Sort.by("eventDate") : Sort.by("views"));
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, sort);

        QEvent event = QEvent.event;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(event.state.eq(EventState.PUBLISHED));

        if (!StringUtils.isNullOrEmpty(eventFilterParams.getText())) {
            booleanBuilder.andAnyOf(event.description.containsIgnoreCase(eventFilterParams.getText()),
                    event.annotation.containsIgnoreCase(eventFilterParams.getText()));
        }
        if (!ObjectUtils.isEmpty(eventFilterParams.getCategories())) {
            booleanBuilder.and(event.category.id.in(eventFilterParams.getCategories()));
        }
        if (eventFilterParams.getPaid() != null) {
            booleanBuilder.and(event.paid.eq(eventFilterParams.getPaid()));
        }
        if (eventFilterParams.getRangeStart() != null) {
            booleanBuilder.and(event.eventDate.after(eventFilterParams.getRangeStart()));
        }
        if (eventFilterParams.getRangeEnd() != null) {
            booleanBuilder.and(event.eventDate.before(eventFilterParams.getRangeEnd()));
        }
        if (eventFilterParams.getRangeStart() != null && eventFilterParams.getRangeEnd() != null) {
            booleanBuilder.and(event.eventDate.after(LocalDateTime.now()));
        }

        List<Event> events = eventRepository.findAll(booleanBuilder.getValue(), pageable).getContent();
        if (eventFilterParams.getOnlyAvailable() != null && eventFilterParams.getOnlyAvailable()) {
            events = events.stream()
                    .filter(evt -> (evt.getParticipantLimit() != 0L)
                            && (evt.getParticipantLimit() >
                            requestRepository.findRequestsByEvent_IdAndStatus(evt.getId(), RequestStatus.CONFIRMED).size()))
                    .collect(Collectors.toList());
        }

        for (Event evt : events) {
            evt.setViews(evt.getViews() + 1);
        }
        if (!events.isEmpty()) {
            eventRepository.saveAll(events);
        }

        return events;
    }

    @Override
    public Event getEventByIdPublic(Long id, HttpServletRequest request) {

        statClient.addHit(
                new EndpointHitDto(request.getServerName(), request.getRequestURI(),
                        request.getRemoteAddr(), LocalDateTime.now())
        );

        Event event = eventRepository.findEventByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new EventNotFoundException(id));

        event.setViews(event.getViews() + 1);
        eventRepository.save(event);

        return event;
    }

    @Override
    public List<Event> getAllEventsByUserPrivate(Long userId, Integer from, Integer size) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        return eventRepository.findEventByInitiator_Id(userId, pageable).getContent();
    }

    @Override
    public Event updateEventByUserPrivate(Long userId, Event updateEvent) {

        Event event = eventRepository.findById(updateEvent.getId())
                .orElseThrow(() -> new EventNotFoundException(updateEvent.getId()));

        if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента.");
        }

        if (event.getState().equals(EventState.CANCELED)) {
            event.setState(EventState.PENDING);
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Изменить можно только отмененные события или события в состоянии " +
                    "ожидания модерации");
        }

        Event mergedEvent = eventMapper.mergeUpdate(updateEvent, event);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        mergedEvent.setInitiator(user);

        Long categoryId = updateEvent.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        mergedEvent.setCategory(category);

        return eventRepository.save(mergedEvent);
    }

    @Override
    public Event addEventByUserPrivate(Long userId, Event newEvent) {

        if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Дата и время на которые намечено событие не может быть раньше, " +
                    "чем через два часа от текущего момента.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        newEvent.setInitiator(user);

        Long categoryId = newEvent.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        newEvent.setCategory(category);

        return eventRepository.save(newEvent);
    }

    @Override
    public Event getEventByUserIdAndEventIdPrivate(Long userId, Long eventId) {
        return eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));
    }

    @Override
    public Event cancellationEventByUserIdAndEventIdPrivate(Long userId, Long eventId) {

        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Отменить можно только событие в состоянии ожидания модерации.");
        }

        event.setState(EventState.CANCELED);
        return eventRepository.save(event);
    }

    @Override
    public List<Request> getEventRequestsByUserPrivate(Long userId, Long eventId) {
        return requestRepository.findRequestsByEvent_IdAndEvent_Initiator_Id(eventId, userId);
    }

    @Override
    public Request confirmEventRequestsByUserPrivate(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));
        Request request = requestRepository.findRequestByIdAndStatus(reqId, RequestStatus.PENDING)
                .orElseThrow(() -> new RequestNotFoundException(reqId));

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0L
                || event.getConfirmedRequests() < event.getParticipantLimit()) {
            request.setStatus(RequestStatus.CONFIRMED);
            request = requestRepository.save(request);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        // Если лимит превышен, отменяем оставшиеся заявки
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            List<Request> requestList = requestRepository.findRequestsByEvent_IdAndStatus(eventId, RequestStatus.PENDING);
            requestList = requestList.stream().peek(req -> req.setStatus(RequestStatus.CANCELED)).collect(Collectors.toList());
            requestRepository.saveAll(requestList);
            throw new ValidationException("Достигнут лимит участников.");
        }

        return request;
    }

    @Override
    public Request rejectEventRequestsByUserPrivate(Long userId, Long eventId, Long reqId) {

        Event event = eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new EventNotFoundException(eventId, userId));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new RequestNotFoundException(reqId));
        request.setStatus(RequestStatus.REJECTED);
        return requestRepository.save(request);
    }

    @Override
    public List<Event> getAllEventsAdmin(EventFilterParamsAdmin eventFilterParams, Integer from, Integer size) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        QEvent event = QEvent.event;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!ObjectUtils.isEmpty(eventFilterParams.getUsers())) {
            booleanBuilder.and(event.initiator.id.in(eventFilterParams.getUsers()));
        }

        if (!ObjectUtils.isEmpty(eventFilterParams.getStates())) {
            booleanBuilder.and(event.state.in(eventFilterParams.getStates()));
        }

        if (!ObjectUtils.isEmpty(eventFilterParams.getCategories())) {
            booleanBuilder.and(event.category.id.in(eventFilterParams.getCategories()));
        }

        if (eventFilterParams.getRangeStart() != null) {
            booleanBuilder.and(event.eventDate.after(eventFilterParams.getRangeStart()));
        }
        if (eventFilterParams.getRangeEnd() != null) {
            booleanBuilder.and(event.eventDate.before(eventFilterParams.getRangeEnd()));
        }

        List<Event> events = eventRepository.findAll(booleanBuilder.getValue(), pageable).getContent();

        return events;
    }

    @Override
    public Event updateEventAdmin(Long eventId, Event updateEvent) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        Event mergedEvent = eventMapper.mergeUpdate(updateEvent, event);

        Long categoryId = updateEvent.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        mergedEvent.setCategory(category);

        return eventRepository.save(mergedEvent);
    }

    @Override
    public Event publishEventAdmin(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации.");
        }

        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Событие должно быть в состоянии ожидания публикации.");
        }

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return eventRepository.save(event);
    }

    @Override
    public Event rejectEventAdmin(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Обратите внимание: событие не должно быть опубликовано.");
        }

        event.setState(EventState.CANCELED);

        return eventRepository.save(event);
    }
}
