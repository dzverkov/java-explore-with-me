package ru.practicum.server.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.server.stat.dto.EndpointHitDto;

import static org.springframework.http.RequestEntity.post;

@RestController
public class StatClient {

    private final RestTemplate restTemplate;
    @Value("${explore-with-me-stats-server.url}")
    private String statServiceUrl;

    @Autowired
    public StatClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public void addHit(EndpointHitDto endPointHitDto) {
        restTemplate.postForEntity(statServiceUrl + "/hit", endPointHitDto, EndpointHitDto.class);
        post("/hit", endPointHitDto);
    }
}
