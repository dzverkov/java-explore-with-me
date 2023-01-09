package ru.practicum.server.events.service;

import ru.practicum.server.events.model.Event;
import ru.practicum.server.events.model.EventFilterParamsAdmin;
import ru.practicum.server.events.model.EventFilterParamsPublic;
import ru.practicum.server.request.model.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<Event> getAllEventsPublic(EventFilterParamsPublic eventFilterParams,
                                   Integer from, Integer size, HttpServletRequest request);

    Event getEventByIdPublic(Long id, HttpServletRequest request);

    List<Event> getAllEventsByUserPrivate(Long userId, Integer from, Integer size);

    Event updateEventByUserPrivate(Long userId, Event updateEvent);

    Event addEventByUserPrivate(Long userId, Event newEvent);

    Event getEventByUserIdAndEventIdPrivate(Long userId, Long eventId);

    Event cancellationEventByUserIdAndEventIdPrivate(Long userId, Long eventId);

    List<Request> getEventRequestsByUserPrivate(Long userId, Long eventId);

    Request confirmEventRequestsByUserPrivate(Long userId, Long eventId, Long reqId);

    Request rejectEventRequestsByUserPrivate(Long userId, Long eventId, Long reqId);

    List<Event> getAllEventsAdmin(EventFilterParamsAdmin eventFilterParams, Integer from, Integer size);

    Event updateEventAdmin(Long eventId, Event updateEvent);

    Event publishEventAdmin(Long eventId);

    Event rejectEventAdmin(Long eventId);
}
