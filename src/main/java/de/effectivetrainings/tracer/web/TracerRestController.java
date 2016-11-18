package de.effectivetrainings.tracer.web;

import de.effectivetrainings.tracer.domain.ServiceCall;
import de.effectivetrainings.tracer.domain.TimeSpan;
import de.effectivetrainings.tracer.domain.graph.InfluxQueryResult;
import de.effectivetrainings.tracer.repository.TracerRepository;
import de.effectivetrainings.tracer.ui.cytoscape.CytoscapeElementMap;
import de.effectivetrainings.tracer.ui.cytoscape.mapper.CytoscapeElementMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
public class TracerRestController {

    private TracerRepository tracerRepository;
    private CytoscapeElementMapper cytoscapeElementMapper;

    public TracerRestController(TracerRepository tracerRepository) {
        this.tracerRepository = Objects.requireNonNull(tracerRepository);
        cytoscapeElementMapper = new CytoscapeElementMapper();
    }

    @RequestMapping("/elements")
    public CytoscapeElementMap elements(@RequestParam(value = "to") Long to) {
        final Set<ServiceCall> calls = tracerRepository.findCalls(new Date(to));
        return cytoscapeElementMapper.map(calls);
    }

    @RequestMapping("/range")
    public TimeSpan timeSpan() {
        return tracerRepository.findTimespan().orElse(null);
    }


    @RequestMapping("/callDuration")
    public List<InfluxQueryResult> callDurations(@RequestParam(value = "to") Long to) {
       return tracerRepository.findCallDurations(new Date(to));
    }

}
