package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Database.Data;
import com.example.SpringMapMatching.Database.LocationNavPath;
import com.example.SpringMapMatching.Model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class _MappingWithoutViterbiService {


    private Data data; // Autowire your Data repository

    @Autowired
    public _MappingWithoutViterbiService(Data data){
        this.data = data;
    }

    @Autowired
    private RoadGraph roadGraph;

    @Autowired
    private SegmentGraph segmentGraph;
//    @Autowired
//    public static SegmentGraph segmentGraph;

    //public static int most_likely_path = -1;

    public Point mapping(List<Double> currObs, Map<Integer, List<List<Double>>> segmentMapping) {
        List<LocationNavPath> nearestSegment = data.findByLocationNear(currObs.get(0), currObs.get(1), 100); // Adjust max distance as needed
        Point mappedPoint = null;

        for (LocationNavPath segment : nearestSegment) {
            int cns_id = segment.getH_ID();
            System.out.println(cns_id + " " + "mls : " + _NewViterbiService.most_likely_path);

            if (cns_id == _NewViterbiService.most_likely_path) {

                List<List<Double>> segmentData = segmentMapping.get(cns_id);
                mappedPoint = findNearestPointFromRoad(new Point(currObs.get(0), currObs.get(1)), segmentData);
                return mappedPoint;
            } else {
                boolean isConnected = _ConnectionOrIntersectionService.connectionExist(_NewViterbiService.most_likely_path, cns_id, segmentGraph);

                if (isConnected) {
                    boolean isIntersection = _ConnectionOrIntersectionService.intersectionExist(_NewViterbiService.most_likely_path, cns_id, roadGraph);
                    System.out.println("A " + isIntersection +  " " +  cns_id + " " + "mls : " + _NewViterbiService.most_likely_path);
                    if (!isIntersection) {
                        _NewViterbiService.most_likely_path = cns_id;
                        List<List<Double>> segmentData = segmentMapping.get(cns_id);
                        mappedPoint = findNearestPointFromRoad(new Point(currObs.get(0), currObs.get(1)), segmentData);
                        System.out.println("xyz");
                        return mappedPoint;
                    }
                }
            }
        }
        return null;
    }

    private static Point findNearestPointFromRoad(Point p, List<List<Double>> roadSegment) {
        Point result = null;
        Double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < roadSegment.size(); i++) {
            Point ptOnRoad = new Point(roadSegment.get(i).get(0), roadSegment.get(i).get(1));
            double distance = Point.haversineDistance(p, ptOnRoad);
            if (distance < minDistance) {
                minDistance = distance;
                result = ptOnRoad;
            }
        }
        return result;
    }
}

