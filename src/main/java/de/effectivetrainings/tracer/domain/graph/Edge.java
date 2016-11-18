package de.effectivetrainings.tracer.domain.graph;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

@Value
@Getter
public class Edge {

    @NonNull
    private String source;
    @NonNull
    private String target;

}
