package de.effectivetrainings.tracer.ui.cytoscape;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CytoscapeEdge {

    private EdgeInfo data;

    @AllArgsConstructor
    @Getter
    public static class EdgeInfo {
        private String source;
        private String target;
    }
}
