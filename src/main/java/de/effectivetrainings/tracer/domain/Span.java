package de.effectivetrainings.tracer.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class Span {

    @NonNull
    private String spanId;
    @NonNull
    private List<Trace> traces;

}
