package de.effectivetrainings.tracer.ui.cytoscape;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CytoscapeElement {

    private ElementInfo data;

    @AllArgsConstructor
    @Getter
    public static class ElementInfo {
        private String id;
        private String name;
    }
}


