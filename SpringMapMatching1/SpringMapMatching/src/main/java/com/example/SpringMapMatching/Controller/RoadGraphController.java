package com.example.SpringMapMatching.Controller;

import com.example.SpringMapMatching.Service.RoadGraph;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoadGraphController {

    private final RoadGraph roadGraph;

    @Autowired
    public RoadGraphController(RoadGraph roadGraph) {
        this.roadGraph = roadGraph;
    }

    @GetMapping("/shortestDistance")
    public double shortestDistance(
            @RequestParam String sourceID,
            @RequestParam String targetID
    ) {
        Graph<String, DefaultWeightedEdge> graph = roadGraph.getRoadGraph();
        return roadGraph.getShortestDistance(graph, sourceID, targetID);
    }


    @GetMapping("/roadMap")
    public Graph<String, DefaultWeightedEdge> roadMap(){
        System.out.println("Road Graph Vertex Loaded");
        return roadGraph.getRoadGraph();
    }
}
