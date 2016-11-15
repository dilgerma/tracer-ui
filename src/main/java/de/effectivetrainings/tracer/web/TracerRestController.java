package de.effectivetrainings.tracer.web;

import de.effectivetrainings.tracer.domain.ServiceCall;
import de.effectivetrainings.tracer.domain.TimeSpan;
import de.effectivetrainings.tracer.repository.TracerRepository;
import de.effectivetrainings.tracer.ui.cytoscape.CytoscapeElementMap;
import de.effectivetrainings.tracer.ui.cytoscape.mapper.CytoscapeElementMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CytoscapeElementMap elements() {
        final Set<ServiceCall> calls = tracerRepository.findCalls();
        return cytoscapeElementMapper.map(calls);
    }

    @RequestMapping("/range")
    public TimeSpan timeSpan() {
        return tracerRepository.findTimespan().orElse(null);
    }
}
