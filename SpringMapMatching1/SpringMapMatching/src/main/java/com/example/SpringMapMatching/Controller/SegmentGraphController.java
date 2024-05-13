package com.example.SpringMapMatching.Controller;

import com.example.SpringMapMatching.Service.SegmentGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/segment-graph")
public class SegmentGraphController {

    private final SegmentGraph segmentGraph;

    @Autowired
    public SegmentGraphController(SegmentGraph segmentGraph) {
        this.segmentGraph = segmentGraph;
    }

    @GetMapping("/graph")
    public Graph<Integer, DefaultEdge> getGraph() {
        System.out.println("Graph called");
        return segmentGraph.getGraph();
    }
}