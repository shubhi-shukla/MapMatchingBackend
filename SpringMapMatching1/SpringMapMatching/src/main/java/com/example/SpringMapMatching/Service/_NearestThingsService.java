package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Database.Data;
import com.example.SpringMapMatching.Database.LocationNavPath;
import com.example.SpringMapMatching.Model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.SpringMapMatching.Service._NewViterbiService.nearbySegments;

@Component
public class _NearestThingsService {
    private final Data data;


    @Autowired
    public _NearestThingsService(Data data) {
        this.data = data;
    }

    public  Map<Integer, Integer> calculatingNearestSegment(List<Double[]> gpsCoordinates) {
        Map<Integer, Integer> segmentToState = new HashMap<>();
        int countOfState = 0;

        for (int i = 0; i < gpsCoordinates.size(); i++) {
            List<LocationNavPath> nearestRoadSegments = data.findByLocationNear(gpsCoordinates.get(i)[0], gpsCoordinates.get(i)[1], 100);
            ArrayList<Integer> segments = new ArrayList<>();
            for (LocationNavPath segment : nearestRoadSegments) {
                int segmentID = segment.getH_ID();
                if (!segmentToState.containsKey(segmentID)) {
                    segmentToState.put(segmentID, countOfState);
                    _CalculateResultService.stateToSegmentID.put(countOfState, segmentID);
                    countOfState++;
                }
                segments.add(segmentID);
            }
            nearbySegments.add(segments);
        }
        return segmentToState;
    }

    public Point[][] calculatingClosestPoints(Map<Integer, Integer> segmentToState, List<Double[]> gpsCoordinates) {
        Point[][] closestPoints = new Point[gpsCoordinates.size()][segmentToState.size()];
        for (int i = 0; i < gpsCoordinates.size(); i++) {
            ArrayList<Integer> closestSegments = new ArrayList<>(nearbySegments.get(i));
            for (int j = 0; j < closestSegments.size(); j++) {
                Integer segmentID = closestSegments.get(j);
                LocationNavPath segments = data.findByH_ID(segmentID);
                List<List<Double>> geoCoordinates = segments.getGeo().getCoordinates();
                closestPoints[i][segmentToState.get(segmentID)] = findNearestPointFromRoad(new Point(gpsCoordinates.get(i)[0], gpsCoordinates.get(i)[1]), geoCoordinates);
            }
        }
        return closestPoints;
    }

//    public static Point[][] calculatingClosestPoints(int startWindow, int endWindow, Map<Integer, Integer> segmentToState){
//        Point[][] closestPoints = new Point[endWindow - startWindow][segmentToState.size()];
//
//        for(int i = startWindow; i < endWindow; i++){
//            ArrayList<Integer> closestSegments = nearbySegments.get(i - startWindow);
//            for(int j = 0; j < closestSegments.size(); j++){
//                int segmentID = closestSegments.get(j);
//                closestPoints[i - startWindow][segmentToState.get(segmentID)] = RoadSegmentDataUploader.findNearestPointFromRoad(new Point(gpsCoordinates.get(i)[0], gpsCoordinates.get(i)[1]), segmentMapping.get(segmentID));
//            }
//        }
//        return closestPoints;
//    }

    private Point findNearestPointFromRoad(Point p, List<List<Double>> roadSegment) {
        Point result = null;
        Double minDistance = Double.MAX_VALUE;
        Double distance = 0.0;

        for (int i = 0; i < roadSegment.size(); i++) {
            Point ptOnRoad = new Point(roadSegment.get(i).get(0), roadSegment.get(i).get(1));
            distance = Point.haversineDistance(p, ptOnRoad);
            if (distance <= minDistance) {
                minDistance = distance;
                result =  new Point(roadSegment.get(i).get(0), roadSegment.get(i).get(1));
            }
        }
        return result;
    }

    public static Point findNearestPointFromRoad(Point p, Double[][] roadSegment){
        Point result = null;
        Double mindistance = Double.MAX_VALUE;
        Double distance = 0.0;

        for(int i = 0; i < roadSegment.length; i++){
            Point ptOnRoad = new Point(roadSegment[i][0], roadSegment[i][1]);
            distance = Point.haversineDistance(p, ptOnRoad);
//            System.out.println(i);
//            System.out.println("------------------" + distance);
//            System.out.println(ptOnRoad.getLongitude() + "," + ptOnRoad.getLatitude());
            if(distance <= mindistance){
                mindistance = distance;
                result = new Point(roadSegment[i][0], roadSegment[i][1]);
            }
        }
//        System.out.println("final ------------------" + distance);
//        System.out.println(result.getLongitude() + "," + result.getLatitude());
        return result;
    }

//    public static Point findNearestPointFromRoad(Point p, Double[][] roadSegment){
//        Point result = null;
//        Double mindistance = Double.MAX_VALUE;
//        Double distance = 0.0;
//
//        for(int i = 0; i < roadSegment.length; i++){
//            Point ptOnRoad = new Point(roadSegment[i][0], roadSegment[i][1]);
//            distance = Point.haversineDistance(p, ptOnRoad);
////            System.out.println(i);
////            System.out.println("------------------" + distance);
////            System.out.println(ptOnRoad.getLongitude() + "," + ptOnRoad.getLatitude());
//            if(distance <= mindistance){
//                mindistance = distance;
//                result = new Point(roadSegment[i][0], roadSegment[i][1]);
//            }
//        }
////        System.out.println("final ------------------" + distance);
////        System.out.println(result.getLongitude() + "," + result.getLatitude());
//        return result;
//    }
}
