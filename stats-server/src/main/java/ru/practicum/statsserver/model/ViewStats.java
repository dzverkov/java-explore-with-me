package ru.practicum.statsserver.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViewStats {

    private String app;

    private String uri;

    private String ip;

    private Long hits;

    public StatGroupBy getStatGroupBy() {
        return new StatGroupBy(app, uri);
    }

    @Data
    @AllArgsConstructor
    public class StatGroupBy {
        private String app;
        private String uri;
    }
}
