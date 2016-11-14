package de.effectivetrainings.tracer.ui.cytoscape;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * {
     nodes: [
       { data: { id: 'j', name: 'Jerry' } },
       { data: { id: 'e', name: 'Elaine' } },
       { data: { id: 'k', name: 'Kramer' } },
       { data: { id: 'g', name: 'George' } }
     ],
     edges: [
       { data: { source: 'j', target: 'e' } },
       { data: { source: 'j', target: 'k' } },
       { data: { source: 'j', target: 'g' } },
       { data: { source: 'e', target: 'j' } },
       { data: { source: 'e', target: 'k' } },
       { data: { source: 'k', target: 'j' } },
       { data: { source: 'k', target: 'e' } },
       { data: { source: 'k', target: 'g' } },
       { data: { source: 'g', target: 'j' } }
     ]
   },
 */
@AllArgsConstructor
@Getter
public class CytoscapeElementMap {

    private List<CytoscapeElement> nodes;
    private Set<CytoscapeEdge> edges;
}
