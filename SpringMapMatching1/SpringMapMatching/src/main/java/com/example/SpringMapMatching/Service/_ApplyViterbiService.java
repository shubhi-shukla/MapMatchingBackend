package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Model.Point;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class _ApplyViterbiService {

    @Autowired
    private RoadGraph roadGraph;

    @Autowired
    private SegmentGraph segmentGraph;

    @Autowired
    private _NearestThingsService _nearestThingsService;

    @Autowired
    private _CalculateResultService _calculateResultService;


    public List<Point> applyViterbi(List<Double[]> gpsCoordinates) {
        Map<Integer, Integer> segmentToState = _nearestThingsService.calculatingNearestSegment(gpsCoordinates);
        double[][] viterbi = new double[gpsCoordinates.size()][segmentToState.size()];
        int[][] backpointer = new int[gpsCoordinates.size()][segmentToState.size()];

        for (int i = 0; i < backpointer.length; i++) {
            Arrays.fill(backpointer[i], -1);
        }

        Point[][] closestPoints = _nearestThingsService.calculatingClosestPoints(segmentToState, gpsCoordinates);
        List<Point> result = new ArrayList<>();


        ArrayList<Integer> initialClosestSegments = _NewViterbiService.nearbySegments.get(0);


        if (_NewViterbiService.isBeginning) {
            for (Integer currClosestSegment : initialClosestSegments) {
                Point currClosestPoint = closestPoints[0][segmentToState.get(currClosestSegment)];
                double ep = _ProbabilitiesService.calcEP(gpsCoordinates.get(0), currClosestPoint);
                viterbi[0][segmentToState.get(currClosestSegment)] = ep;
                backpointer[0][segmentToState.get(currClosestSegment)] = segmentToState.get(currClosestSegment);
            }
        } else {
            Set<DefaultEdge> edges = segmentGraph.getGraph().edgesOf(_NewViterbiService.most_likely_path);
            Set<Integer> connectedVertices = new HashSet<>();
            int cnt = edges.size();

            for (DefaultEdge edge : edges) {
                int sourceVertex = segmentGraph.getGraph().getEdgeSource(edge);
                int targetVertex = segmentGraph.getGraph().getEdgeTarget(edge);
                int connectedVertex = (sourceVertex == _NewViterbiService.most_likely_path) ? targetVertex : sourceVertex;
                connectedVertices.add(connectedVertex);
            }

            int ctr = 0;

            for(int i = 0; i < initialClosestSegments.size(); i++){
                Integer currClosestSegment = initialClosestSegments.get(i);
                Point currClosestPoint = closestPoints[0][segmentToState.get(currClosestSegment)];

                double ep = 0.0;
                if(connectedVertices.contains(currClosestSegment)){
                    ep = (1.00 / cnt);
                    ctr++;
                }
                else{
                    ep = 0.0;
                }
                viterbi[0][segmentToState.get(currClosestSegment)] = ep;
                backpointer[0][segmentToState.get(currClosestSegment)] = segmentToState.get(currClosestSegment);
            }

            if(ctr == 0){
                Set<Integer> moreConnectedVertices = getVerticesAtDistance(segmentGraph, _NewViterbiService.most_likely_path, 3);
                for(int i = 0; i < initialClosestSegments.size(); i++){
                    double ep =  0.0;
                    Integer currClosestSegment = initialClosestSegments.get(i);
                    if(moreConnectedVertices.contains(currClosestSegment)){
                        ep = (1.00 / cnt);
                    }
                    else{
                        ep = 0.0;
                    }
                    viterbi[0][segmentToState.get(currClosestSegment)] = ep;
                    backpointer[0][segmentToState.get(currClosestSegment)] = segmentToState.get(currClosestSegment);
                }
            }
        }

        for (int i = 1; i < gpsCoordinates.size(); i++) {
            ArrayList<Integer> currClosestSegments = _NewViterbiService.nearbySegments.get(i);
            for (Integer currSegment : currClosestSegments) {
                Point currPoint = closestPoints[i][segmentToState.get(currSegment)];
                double maxProb = Double.MIN_VALUE;
                int maxState = -1;
                double ep = _ProbabilitiesService.calcEP(gpsCoordinates.get(i), currPoint);

                for (int k = 0; k < segmentToState.size(); k++) {
                    Integer prevSegment = _CalculateResultService.stateToSegmentID.get(k);
                    Point prevPoint = closestPoints[i - 1][segmentToState.get(prevSegment)];
                    double tp = (prevPoint != null) ?
                            _ProbabilitiesService.calcTP(gpsCoordinates.get(i - 1), gpsCoordinates.get(i), prevPoint, currPoint) :
                            0.0;
                    double prob = (viterbi[i - 1][k] != 0.0) ?
                            viterbi[i - 1][k] * tp * ep :
                            Double.MIN_VALUE;

//                    if(sw == 0){
//                        System.out.println(i + " " + prevSegment + " " + currSegment + " prev prob " + viterbi[i - 1 - sw][k] + " tp : " +  tp + " ep " + ep + " curr prob : " + prob);
//
//                    }
                    if (prob > maxProb) {
                        maxProb = prob;
                        maxState = k;
                    }
                }

                viterbi[i][segmentToState.get(currSegment)] = maxProb;
                backpointer[i][segmentToState.get(currSegment)] = maxState;
            }

            if (i == gpsCoordinates.size() - 1) {
                result = _calculateResultService.calculateResult(viterbi, backpointer, segmentToState, closestPoints, gpsCoordinates);
            }
        }

        _NewViterbiService.nearbySegments.clear();
        _CalculateResultService.stateToSegmentID.clear();
        return result;
    }

    public static Set<Integer> getVerticesAtDistance(SegmentGraph segmentGraph, int sourceVertex, int distance) {
        Set<Integer> vertices = new HashSet<>();

        // Perform BFS starting from the source vertex
        BreadthFirstIterator<Integer, DefaultEdge> bfsIterator = new BreadthFirstIterator<>(segmentGraph.getGraph(), sourceVertex);

        // Iterate over vertices at each level of the BFS tree
        while (bfsIterator.hasNext()) {
            Integer vertex = bfsIterator.next();
            int currentDistance = bfsIterator.getDepth(vertex);
            if (currentDistance >= 2 && currentDistance <= distance) {
                vertices.add(vertex);
            }
            if (currentDistance > distance) {
                break;
            }
        }

        return vertices;
    }
}
