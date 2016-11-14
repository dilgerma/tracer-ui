package de.effectivetrainings.tracer.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServiceInstance {
    @NonNull
    private String name;
}
