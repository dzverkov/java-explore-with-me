package ru.practicum.server.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.events.exception.EventNotFoundException;
import ru.practicum.server.events.model.Event;
import ru.practicum.server.events.model.EventState;
import ru.practicum.server.events.repository.EventRepository;
import ru.practicum.server.exception.ValidationException;
import ru.practicum.server.request.exception.RequestNotFoundException;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.RequestStatus;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.users.exception.UserNotFoundException;
import ru.practicum.server.users.model.User;
import ru.practicum.server.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    @Override
    public List<Request> getRequestsByUserId(Long userId) {

        return requestRepository.findRequestsByRequester_Id(userId);
    }

    @Override
    public Request addRequest(Long userId, Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException(eventId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Optional<Request> request = requestRepository.findRequestByEvent_IdAndRequester_Id(eventId, userId);
        if (request.isPresent()) {
            throw new ValidationException("Такой запрос уже существует, нельзя добавить повторный запрос.");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ValidationException("Инициатор события не может добавить запрос на участие в своём событии.");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Нельзя участвовать в неопубликованном событии.");
        }

        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ValidationException("У события достигнут лимит запросов на участие.");
        }

        Request newRequest = new Request();
        newRequest.setEvent(event);
        newRequest.setRequester(user);

        if (!event.getRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        }

        return requestRepository.save(newRequest);
    }

    @Override
    public Request cancelRequest(Long userId, Long requestId) {

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException(requestId));

        if (!request.getRequester().getId().equals(userId)) {
            throw new ValidationException("Запрос подавал другой пользователь. Отменить можно только свой запрос.");
        }
        request.setStatus(RequestStatus.CANCELED);

        return requestRepository.save(request);
    }
}
