package ru.practicum.server.locations.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import ru.practicum.server.events.model.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Immutable
@Table(name = "v_event_by_location", schema = "public")
public class EventFullWithLocation {
    @Id
    private Long id;
    @Column(name = "event_id")
    private Long eventId;
    private String annotation;
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "category_name")
    private String categoryName;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "initiator_id")
    private Long initiatorId;
    @Column(name = "initiator_email")
    private String initiatorEmail;
    @Column(name = "initiator_name")
    private String initiatorName;
    @Column(name = "location_id")
    private Long locationId;
    private Float lat;
    private Float lon;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    @Column(name = "event_location_id")
    private Long eventLocationId;
    @Column(name = "event_location_name")
    private String eventLocationName;
    @Column(name = "event_location_type_id")
    private Long eventLocationTypeId;
    @Column(name = "event_location_type")
    private String eventLocationType;
    @Column(name = "event_location_lat")
    private Float eventLocationLat;
    @Column(name = "event_location_lon")
    private Float eventLocationLon;
    @Column(name = "event_location_radius")
    private Float eventLocationRadius;
    private Float distance;
}
