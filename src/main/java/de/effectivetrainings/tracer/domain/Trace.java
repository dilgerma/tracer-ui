package de.effectivetrainings.tracer.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Trace {

    public static final String UNKNOWN_SOURCE_TARGET = "unknown";

    @NonNull
    private String source;
    @NonNull
    private String target;
    @NonNull
    private String traceId;
    @NonNull
    private long duration;


    public boolean isComplete() {
        return !UNKNOWN_SOURCE_TARGET.equals(source) && !UNKNOWN_SOURCE_TARGET.equals(target);
    }
}
