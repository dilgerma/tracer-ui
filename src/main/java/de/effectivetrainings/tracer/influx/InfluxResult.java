package de.effectivetrainings.tracer.influx;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class InfluxResult {

    private final Map<String, String> fields;
    private final Map<String, String> tags;

}
