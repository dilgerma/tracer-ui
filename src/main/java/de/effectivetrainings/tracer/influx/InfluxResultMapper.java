package de.effectivetrainings.tracer.influx;

import com.google.common.base.Preconditions;
import org.influxdb.dto.QueryResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InfluxResultMapper {

    public List<InfluxResult> mapSingleResult(QueryResult query) {
        return findSeries(query).stream().map(serie -> {
                   final Map<String, String> tags = serie.getTags();
                   final List<String> columns = serie.getColumns();
                   Preconditions.checkArgument(serie
                           .getValues()
                           .size() == 1, "'more than one value list received. Please verify your Query'");
                   final List<Object> values = serie
                           .getValues()
                           .get(0);

                   Map<String, String> fields = new HashMap<>();
                   for (int i = 0; i < columns
                           .size(); i++) {
                       final int cnt = i;
                       fields.compute(
                               columns.get(i),
                               (key, value)->values.get(cnt) != null ? values.get(cnt).toString() : "0");
                   }
                   return new InfluxResult(fields, tags);
        }).collect(Collectors.toList());


    }

    private List<QueryResult.Series> findSeries(QueryResult query) {
        return query
                .getResults()
                .stream()
                .filter(result -> result.getSeries() != null)
                .map(QueryResult.Result::getSeries)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
