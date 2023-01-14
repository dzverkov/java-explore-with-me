package ru.practicum.server.request.repository;

public interface RequestsCountByEvent {
    Long getEventId();

    Long getReqCount();
}
