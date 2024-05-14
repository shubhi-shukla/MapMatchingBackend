package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Database.Data;
import com.example.SpringMapMatching.Database.LocationNavPath;
import jakarta.annotation.PostConstruct;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SegmentGraph {

    @Autowired
    private Data data;

    private static Graph<Integer, DefaultEdge> graph;
    @PostConstruct
    public void init() {
        graph = buildSegmentGraph();
//        System.out.println("SEGMENT GRAPH CREATED");
//        printVertexCount();
    }

    public static Graph<Integer, DefaultEdge> getGraph() {
        return graph;
    }

    public Graph<Integer, DefaultEdge> buildSegmentGraph() {
        Graph<Integer, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        // Fetch road segments data using your Data repository
        List<LocationNavPath> roadSegments = data.findAll(); // Example method, adjust as needed


        for (LocationNavPath segment : roadSegments) {
            int segmentID = segment.getH_ID(); // Assuming H_ID is the segment ID
            // Add segmentID as vertex to the graph
            graph.addVertex(segmentID);
        }

        // Build edges based on coordinates
        for (int i = 0; i < roadSegments.size(); i++) {
            List<List<Double>> road1Coordinates = roadSegments.get(i).getGeo().getCoordinates();
            int segment1ID = roadSegments.get(i).getH_ID();

            for (int j = 0; j < roadSegments.size(); j++) {
                if (i != j) {
                    List<List<Double>> road2Coordinates = roadSegments.get(j).getGeo().getCoordinates();
                    int segment2ID = roadSegments.get(j).getH_ID();

                    for (int x = 0; x < road1Coordinates.size(); x += road1Coordinates.size() - 1) {
                        List<Double> coord1 = road1Coordinates.get(x);
                        for (int y = 0; y < road2Coordinates.size(); y += road2Coordinates.size() - 1) {
                            List<Double> coord2 = road2Coordinates.get(y);
                            if (coord1.get(0).equals(coord2.get(0)) && coord1.get(1).equals(coord2.get(1))) {
                                if (segment1ID != segment2ID) {
                                    graph.addEdge(segment1ID, segment2ID);
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(graph.vertexSet().size());
        System.out.println("segment graph made");
        return graph;
    }

    public void printVertexCount(){
//        System.out.println("Segment Vertices: " + graph.vertexSet().size());
    }
}
