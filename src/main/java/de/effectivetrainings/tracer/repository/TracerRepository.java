package de.effectivetrainings.tracer.repository;

import de.effectivetrainings.tracer.domain.ServiceCall;
import de.effectivetrainings.tracer.domain.Span;
import de.effectivetrainings.tracer.domain.Trace;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.util.*;
import java.util.stream.Collectors;

public class TracerRepository {

    public static final String SELECT_ALL_GROUP_BY_TRACE_ID = "select traceId,source,target,\"duration\" from trace group by traceIdTag";
    public static final String SELECT_SERVICE_CONNECTIONS = "select source, target from trace where source != 'unknown' and target <> 'unknown'";

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

    private Long duration(List list) {
        final String durationField = String.valueOf(list.get(4));
        try {
            return new Double(Double.parseDouble(durationField)).longValue();
        } catch (NumberFormatException e) {
            return -1l;
        }
    }

}
