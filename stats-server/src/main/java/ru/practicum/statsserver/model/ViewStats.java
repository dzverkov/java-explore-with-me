package ru.practicum.statsserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
