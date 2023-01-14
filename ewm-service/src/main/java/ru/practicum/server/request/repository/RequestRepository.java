package ru.practicum.server.request.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends PagingAndSortingRepository<Request, Long> {

    Optional<Request> findRequestByEvent_IdAndRequester_Id(Long eventId, Long userId);

    Optional<Request> findRequestByIdAndStatus(Long id, RequestStatus status);

    List<Request> findRequestsByEvent_IdAndStatus(Long id, RequestStatus status);

    @Query("select r.event.id as eventId, count(r.id) as reqCount from Request r " +
            "where r.event.id in ?1 and r.status = ?2 group by r.event.id")
    List<RequestsCountByEvent> findCountRequestsByStatus(List<Long> eventIds, RequestStatus status);

    List<Request> findRequestsByRequester_Id(Long userId);

    List<Request> findRequestsByEvent_IdAndEvent_Initiator_Id(Long eventId, Long userId);
}
