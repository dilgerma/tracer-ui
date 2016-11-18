package de.effectivetrainings.tracer.domain.graph;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class InfluxQueryResult {

    private Edge edge;
    private Node node;
    private Map<String, String> edgeData;
    private Map<String, String> nodeData;

}
