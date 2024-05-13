package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Model.Point;
import org.springframework.stereotype.Service;

@Service
public class _ProbabilitiesService {
    private static final double sigma = 0.1;
    private static final double beta = 6.5;
    private static RoadGraph roadGraph;

    public _ProbabilitiesService(RoadGraph roadGraph) {
        this.roadGraph = roadGraph;
    }

    public static double calcEP(Double[] gpsObs, Point currPoint) {
        double distanceToRoad = Point.haversineDistance(new Point(gpsObs[0], gpsObs[1]), currPoint);

        return (1 / (sigma * Math.sqrt(2 * Math.PI))) * Math.exp(-0.5 * Math.pow(distanceToRoad / sigma, 2));
    }

    public static double calcTP(Double[] prevGPSObs, Double[] currGPSObs, Point prevPoint, Point currPoint) {
//        Double[] c1 = new Double[]{currPoint.getLongitude(), currPoint.getLatitude()};
//        Double[] c2 = new Double[]{prevPoint.getLongitude(), prevPoint.getLatitude()};

        String prevVertex = prevPoint.getLongitude() + "#" + prevPoint.getLatitude();
        String currVertex = currPoint.getLongitude() + "#" + currPoint.getLatitude();

        double rd = roadGraph.getShortestDistance(roadGraph.getRoadGraph(), prevVertex, currVertex);

        double gcd = Point.haversineDistance(new Point(prevGPSObs[0], prevGPSObs[1]), new Point(currGPSObs[0], currGPSObs[1]));
        double diff = Math.abs(gcd - rd);
        return Math.exp((-1 * diff) / beta) / beta;
    }
}

