package de.effectivetrainings.tracer.influx;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InfluxConnection {

    @NonNull
    private String url;
    @NonNull
    private String user;
    @NonNull
    private String password;
    @NonNull
    private String database;
}
