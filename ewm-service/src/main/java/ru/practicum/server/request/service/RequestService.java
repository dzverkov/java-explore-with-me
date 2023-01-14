package ru.practicum.server.request.service;

import ru.practicum.server.request.model.Request;

import java.util.List;

public interface RequestService {
    List<Request> getRequestsByUserId(Long userId);

    Request addRequest(Long userId, Long eventId);

    Request cancelRequest(Long userId, Long requestId);
}
