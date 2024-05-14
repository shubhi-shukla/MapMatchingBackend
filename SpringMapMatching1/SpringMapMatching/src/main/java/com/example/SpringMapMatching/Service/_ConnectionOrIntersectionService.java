package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Database.Data;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class _ConnectionOrIntersectionService {
    @Autowired
    private static Data data;

    @Autowired
    private RoadGraph roadGraph;

    @Autowired
    private static SegmentGraph segmentGraph;

//    static Map<Integer, List<List<Double>>> segmentMapping = new HashMap<>();
//    static List<LocationNavPath> segmentsMap = new ArrayList<>();

    public static boolean connectionExist(int vertex1, int vertex2, SegmentGraph segmentGraph) {

        Set<DefaultEdge> edgesOfVertex1 = segmentGraph.getGraph().edgesOf(vertex1);

        // Check if vertex2 is in the adjacency list of vertex1
        for (DefaultEdge edge : edgesOfVertex1) {
            Integer source = segmentGraph.getGraph().getEdgeSource(edge);
            Integer target = segmentGraph.getGraph().getEdgeTarget(edge);
            if ((source.equals(vertex1) && target.equals(vertex2)) || (source.equals(vertex2) && target.equals(vertex1))) {
                return true;
            }
        }
        return false;
    }

    public static boolean intersectionExist(int segment1, int segment2, RoadGraph roadGraph) {

        List<List<Double>> road1Coordinates = _NewViterbiService.segmentMapping.get(segment1);
        List<List<Double>> road2Coordinates = _NewViterbiService.segmentMapping.get(segment2);

        List<Double> road1Start = road1Coordinates.get(0);
        List<Double> road1End = road1Coordinates.get(road1Coordinates.size() - 1);

        List<Double> road2Start = road2Coordinates.get(0);
        List<Double> road2End = road2Coordinates.get(road2Coordinates.size() - 1);

        if ((road1Start.get(0).equals(road2Start.get(0))) && (road1Start.get(1).equals(road2Start.get(1)))){
            if (roadGraph.getRoadGraph().degreeOf(road1Start.get(0) + "#" + road1Start.get(1)) >= 3) {
                return true;
            }
        }

        if ((road1Start.get(0
        ).equals(road2End.get(0
        ))) && (road1Start.get(1).equals(road2End.get(1)))) {
            if (roadGraph.getRoadGraph().degreeOf(road1Start.get(0) + "#" + road1Start.get(1)) >= 3) {
                return true;
            }
        }

        if ((road1End.get(0
        ).equals(road2Start.get(0
        ))) && (road1End.get(1).equals(road2Start.get(1)))) {
            if (roadGraph.getRoadGraph().degreeOf(road1End.get(0) + "#" + road1End.get(1)) >= 3) {
                return true;
            }
        }

        if ((road1End.get(0
        ).equals(road2End.get(0
        ))) && (road1End.get(1).equals(road2End.get(1)))) {
            if (roadGraph.getRoadGraph().degreeOf(road1End.get(0) + "#" + road1End.get(1)) >= 3) {
                return true;
            }
        }

        return false;
    }
}
