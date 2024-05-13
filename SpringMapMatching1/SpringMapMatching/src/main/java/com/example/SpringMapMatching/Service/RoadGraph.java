package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Database.Data;
import com.example.SpringMapMatching.Database.Geo;
import com.example.SpringMapMatching.Database.LocationNavPath;
import com.example.SpringMapMatching.Model.Point;
import jakarta.annotation.PostConstruct;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RoadGraph {

    @Autowired
    private Data data;

    private Graph<String, DefaultWeightedEdge> graph;

    private Point point;

    @PostConstruct
    public void init() {
        graph = buildRoadGraph();

        point = new Point(0.0,0.0);
//        System.out.println("ROAD GRAPH CREATED");
//        printRoadVertex();
    }

    public Graph<String, DefaultWeightedEdge> getRoadGraph() {
        return graph;
    }

    private Graph<String, DefaultWeightedEdge> buildRoadGraph() {
        // Create a weighted graph
        Graph<String, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // Fetch road segments data using your Data repository
        List<LocationNavPath> roadSegments = data.findAll(); // Example method, adjust as needed

        // Set to keep track of unique vertices
        Set<String> uniqueVertices = new HashSet<>();

        for (LocationNavPath segment : roadSegments) {
            Geo geometry = segment.getGeo();
            List<List<Double>> coordinates = geometry.getCoordinates();

            // Add vertices (coordinate points) to the graph
            for (List<Double> coordinate : coordinates) {
                String vertex = coordinate.get(0) + "#" + coordinate.get(1);
                if (!uniqueVertices.contains(vertex)) {
                    graph.addVertex(vertex);
                    uniqueVertices.add(vertex); // Add the vertex to the set
                }
            }

            // Add edges (connections between adjacent points) to the graph
            for (int i = 0; i < coordinates.size() - 1; i++) {
                List<Double> sourceVertex = coordinates.get(i);
                List<Double> targetVertex = coordinates.get(i+1);
                String sv = sourceVertex.get(0) + "#" + sourceVertex.get(1);
                String tv = targetVertex.get(0) + "#" + targetVertex.get(1);

                // Calculate distance between two points using Haversine formula
                double distance = point.haversineDistance(new Point(sourceVertex.get(0), sourceVertex.get(1)), new Point(targetVertex.get(0), targetVertex.get(1)));

                // Add undirected edge with weight
                DefaultWeightedEdge edge = graph.addEdge(sv, tv);
                graph.setEdgeWeight(edge, distance);
            }
        }

        return graph;
    }

    public double getShortestDistance(Graph<String, DefaultWeightedEdge> graph,String sourceVertex, String targetVertex) {
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        return dijkstra.getPathWeight(sourceVertex, targetVertex);
    }

    public void printRoadVertex(){
//        System.out.println("Vertices: " + graph.vertexSet().size());
    }
}
