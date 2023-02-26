package ru.practicum.stats.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;

    public ViewStats(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }

    @Override
    public String toString() {
        return "EndpointHitDto{" +
                "app='" + app + '\'' +
                ", uri='" + uri + '\'' +
                ", hits=" + hits +
                '}';
    }
}
