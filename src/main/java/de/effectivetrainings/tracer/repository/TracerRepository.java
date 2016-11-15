package de.effectivetrainings.tracer.repository;

import de.effectivetrainings.tracer.domain.ServiceCall;
import de.effectivetrainings.tracer.domain.Span;
import de.effectivetrainings.tracer.domain.TimeSpan;
import de.effectivetrainings.tracer.domain.Trace;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TracerRepository {

    public static final String SELECT_ALL_GROUP_BY_TRACE_ID = "select traceId,source,target,\"duration\" from trace group by traceIdTag";
    public static final String SELECT_SERVICE_CONNECTIONS = "select source, target from trace where source != 'unknown' and target <> 'unknown'";

    //workaround for https://github.com/influxdata/influxdb/issues/5793
    public static final String FIRST_ELEMENT = "select first(\"duration\") from trace";
    public static final String LAST_ELEMENT = "select last(\"duration\") from trace";

    private InfluxDB influxDB;
    private String database;

    public TracerRepository(InfluxDB influxDB, String database) {
        this.influxDB = Objects.requireNonNull(influxDB);
        this.database = database;
    }

    public List<Span> findSpans() {
        final QueryResult query = influxDB.query(new Query(SELECT_ALL_GROUP_BY_TRACE_ID, database));
        final Map<String, List<Trace>> traces = query
                .getResults()
                .stream()
                .map(QueryResult.Result::getSeries)
                .flatMap(List::stream)
                .map(series -> series.getValues())
                .flatMap(List::stream)
                .map(obj -> (ArrayList) obj)
                .map(list -> new Trace(String.valueOf(list.get(2)), String.valueOf(list.get(3)), String.valueOf(list.get(1)), duration(list)))
                .collect(Collectors.groupingBy(Trace::getTraceId));

        return traces
                .entrySet()
                .stream()
                .map(entry -> new Span(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public Set<ServiceCall> findCalls() {
        final QueryResult query = influxDB.query(new Query(SELECT_SERVICE_CONNECTIONS, database));
        return query
                .getResults()
                .stream()
                .map(QueryResult.Result::getSeries)
                .flatMap(List::stream)
                .map(series -> series.getValues())
                .flatMap(List::stream)
                .map(obj -> (ArrayList) obj)
                .distinct()
                .map(list -> new ServiceCall(String.valueOf(list.get(1)), String.valueOf(list.get(2))))
                .collect(Collectors.toSet());
    }

    public Optional<TimeSpan> findTimespan() {
        final QueryResult firstResult = influxDB.query(new Query(FIRST_ELEMENT, database));
        final QueryResult lastResult = influxDB.query(new Query(LAST_ELEMENT, database));
        final Optional<Long> startTime = toTimeStamp(firstResult);
        final Optional<Long> endTime = toTimeStamp(lastResult);
        if (startTime.isPresent() && endTime.isPresent()) {
            return Optional.of(TimeSpan.of(startTime.get(), endTime.get()));
        } else {
            return Optional.empty();
        }
    }

    private Optional<Long> toTimeStamp(QueryResult firstResult) {
        return firstResult
                .getResults()
                .stream()
                .map(QueryResult.Result::getSeries)
                .flatMap(List::stream)
                .map(series -> series.getValues())
                .flatMap(List::stream)
                .map(obj -> (ArrayList) obj)
                .distinct()
                .map(list -> fromInfluxDBTimeFormat(list
                        .get(0)
                        .toString()))
                .findFirst();
    }

    //TODO upgrade after influxdb-java 2.5 (TimeUtils)
    public static long fromInfluxDBTimeFormat(String time) {
        try {
            String[] parts = time.split("T");
            String datePart = parts[0];
            String timePart = parts[1].substring(0, parts[1].length() - 1);
            SimpleDateFormat dateDF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeDF = new SimpleDateFormat("HH:mm:ss.SSS");
            dateDF.setTimeZone(TimeZone.getTimeZone("UTC"));
            timeDF.setTimeZone(TimeZone.getTimeZone("UTC"));

            return dateDF
                    .parse(datePart)
                    .getTime() + timeDF
                    .parse(timePart)
                    .getTime();
        } catch (Exception e) {
            throw new RuntimeException("unexpected date format", e);
        }
    }

    private Long duration(List list) {
        final String durationField = String.valueOf(list.get(4));
        try {
            return new Double(Double.parseDouble(durationField)).longValue();
        } catch (NumberFormatException e) {
            return -1l;
        }
    }
}
