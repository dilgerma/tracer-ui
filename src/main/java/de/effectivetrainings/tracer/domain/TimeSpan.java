package de.effectivetrainings.tracer.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class TimeSpan {

    private Date from;
    private Date to;
    public static TimeSpan of(long from, long to) {
        return new TimeSpan(new Date(from), new Date(to));
    }
}
