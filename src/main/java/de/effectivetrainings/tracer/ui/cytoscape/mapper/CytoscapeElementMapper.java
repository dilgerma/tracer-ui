package de.effectivetrainings.tracer.ui.cytoscape.mapper;

import de.effectivetrainings.tracer.domain.ServiceCall;
import de.effectivetrainings.tracer.ui.cytoscape.CytoscapeEdge;
import de.effectivetrainings.tracer.ui.cytoscape.CytoscapeElement;
import de.effectivetrainings.tracer.ui.cytoscape.CytoscapeElementMap;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CytoscapeElementMapper {

    public CytoscapeElementMap map(Set<ServiceCall> serviceCalls) {

        final List<CytoscapeElement> nodes = serviceCalls
                .stream()
                .flatMap(serviceCall -> Stream.of(serviceCall.getSource(), serviceCall.getTarget()))
                .distinct()
                .map(service -> new CytoscapeElement(new CytoscapeElement.ElementInfo(service, service)))
                .sorted((o1, o2) -> o1
                        .getData()
                        .getName()
                        .compareTo(o2
                                .getData()
                                .getName()))
                .collect(Collectors.toList());

        final Set<CytoscapeEdge> edges = serviceCalls
                .stream()
                .map(sc -> new CytoscapeEdge(new CytoscapeEdge.EdgeInfo(sc.getSource(), sc.getTarget())))
                .collect(Collectors.toSet());

        return new CytoscapeElementMap(nodes, edges);
    }
}
