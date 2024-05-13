package com.example.SpringMapMatching.Service;

public class Trash {
}

//for (int j = 0; j < closestSegments.size(); j++) {
//Integer segmentID = closestSegments.get(j);
//List<LocationNavPath> segments = data.findById(segmentID);
//                System.out.println("SEGMENTS CALL : " + segments);
//                if (segments != null && !segments.isEmpty()) {
//        System.out.println("SEGMENT KE ANDAR SEGMENT : " + segments);
//                    for (LocationNavPath segment : segments) {
//List<List<Double>> geoCoordinates = segment.getGeo().getCoordinates();
//Double[][] segmentCoordinates = new Double[geoCoordinates.size()][];
//                        for (int h = 0; h < geoCoordinates.size(); h++) {
//List<Double> coordList = geoCoordinates.get(h);
//segmentCoordinates[h] = coordList.toArray(new Double[0]);
//                        }
//closestPoints[i - startWindow][segmentToState.get(segmentID)] = findNearestPointFromRoad(new Point(gpsCoordinates.get(i)[0], gpsCoordinates.get(i)[1]), segmentCoordinates);
//        }
//        }
//        else{
//        System.out.println("KHALI HUN ME");
//                }
//                        }

//_NEWVITERBISERVICE OLD


//import com.example.SpringMapMatching.Database.Data;
//import com.example.SpringMapMatching.Database.LocationNavPath;
//import com.example.SpringMapMatching.Model.Point;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class _NewViterbiService {
//    public static List<LocationNavPath> roadSegments = new ArrayList<>(); // Road segment coordinates
//    public static List<Double[]> allGPSCoordinates = new ArrayList<>(); // Observation coordinates
//    public static ArrayList<ArrayList<Integer>> nearbySegments = new ArrayList<>();
//
//    @Autowired
//    private final Data data;
//
//    @Autowired
//    private RoadGraph roadGraph;
//
//    @Autowired
//    private final _MappingWithoutViterbiService mappingWithoutViterbi;
//
//    @Autowired
//    private final _ApplyViterbiService applyViterbi;
//
//    public _NewViterbiService(_ApplyViterbiService applyViterbi, _MappingWithoutViterbiService mappingWithoutViterbi, RoadGraph roadGraph, Data data) {
//        this.applyViterbi = applyViterbi;
//        this.mappingWithoutViterbi = mappingWithoutViterbi;
//        this.roadGraph = roadGraph;
//        this.data = data;
//    }
//
//    public List<Point> process(List<Double[]> gpsCoordinates) {
//        roadSegments = data.findAll();
//
//        // Fetch GPS observation coordinates from roadSegments
//        for (LocationNavPath segment : roadSegments) {
//            if (segment.getGeo() != null && segment.getGeo().getCoordinates() != null) {
//                Double[][] coordinates = segment.getGeo().getCoordinates();
//                Double latitude = coordinates[0][0];
//                Double longitude = coordinates[0][1];
//                Double[] coords = new Double[]{latitude, longitude};
//                GPSCoordinates.add(coords);
//            } else {
//                System.out.println("Warning: Missing or empty coordinates for segment " + segment.getId());
//            }
//        }
//
//        int T = gpsCoordinates.size();
//
//        int windowSize = 6;
//
//        int startWindow = 0;
//        int endWindow = windowSize;
//        List<Point> mappedPoints = new ArrayList<>();
//
//        for (int i = 0; i < T; i++) {
//            if (i == 0) {
//                startWindow = i;
//                endWindow = Math.min(i + windowSize, T);
//                mappedPoints = applyViterbi.applyViterbi(startWindow, endWindow);
//                i = endWindow - 1;
//            } else {
//                Point tbc = mappingWithoutViterbi.mapping(gpsCoordinates.get(i), _ConnectionOrIntersectionService.segmentMapping);
//                List<Point> collectionOfPoints = new ArrayList<>();
//                if (tbc != null) {
//                    System.out.println("mls : " + mappingWithoutViterbi.most_likely_path);
//                } else {
//                    startWindow = i;
//                    endWindow = Math.min(i + windowSize, T);
//                    i = Math.min(i + windowSize, T);
//                    System.out.println("sw : " + startWindow + " ew " + endWindow);
//                    mappedPoints = applyViterbi.applyViterbi(startWindow, endWindow);
//                    System.out.println("mls " + _MappingWithoutViterbiService.most_likely_path);
//                    i = endWindow - 1;
//                }
//            }
//        }
//        return mappedPoints;
//    }
//}



