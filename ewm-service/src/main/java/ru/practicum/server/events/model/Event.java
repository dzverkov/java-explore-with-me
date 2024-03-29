package ru.practicum.server.events.model;

import lombok.*;
import ru.practicum.server.categories.model.Category;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @Transient
    private Long confirmedRequests = 0L;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    private User initiator;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    private Boolean paid = false;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    private String title;

    @Transient
    private Long views = 0L;

    @ManyToMany(mappedBy = "events")
    private List<Compilation> compilations = new ArrayList<>();

    public Event(Long id) {
        this.id = id;
    }
}
