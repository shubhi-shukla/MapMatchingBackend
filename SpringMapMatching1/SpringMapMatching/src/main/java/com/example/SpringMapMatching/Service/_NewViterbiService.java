package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Database.Data;
import com.example.SpringMapMatching.Database.LocationNavPath;
import com.example.SpringMapMatching.Model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class _NewViterbiService {

    private Data data;

    @Autowired
    public _NewViterbiService(Data data, _MappingWithoutViterbiService mappingWithoutViterbi, _ApplyViterbiService applyViterbi){
        this.data = data;
        this.mappingWithoutViterbi = mappingWithoutViterbi;
        this.applyViterbi = applyViterbi;
    }

    @Autowired
    private final _MappingWithoutViterbiService mappingWithoutViterbi;

    @Autowired
    private final _ApplyViterbiService applyViterbi;

    static Map<Integer, List<List<Double>>> segmentMapping = new HashMap<>();
    static List<LocationNavPath> segmentsMap = new ArrayList<>();

    public static ArrayList<ArrayList<Integer>> nearbySegments = new ArrayList<>();
    public static boolean checkIntersectionInsideWindow = false;
    public static boolean checkWindowExtention = false;
    public static int extendedWindow = 15;

    public static boolean isBeginning = true;
    public static boolean isIntersection = false;
    static int windowSize = 15;
    static int startWindow = 0;
    static int endWindow = windowSize;
    public static int most_likely_path = -1;
    static  int number = 0;
    public static int makeWindow = -1;

    public List<Point> process(List<Double[]> gpsCoordinates) {
        int T = gpsCoordinates.size();

        List<Point> mappedPoints = new ArrayList<>();
        List<Point> totalPoints = new ArrayList<>();
        segmentsMap = data.findAll();

        if(!segmentsMap.isEmpty()) {
            for (LocationNavPath segment : segmentsMap) {
                segmentMapping.put(segment.getH_ID(), segment.getGeo().getCoordinates());
            }
        } else{
            System.out.println("No segments found in the database.");
        }


        if (isBeginning) {
            mappedPoints = applyViterbi.applyViterbi(gpsCoordinates);
            printResult(mappedPoints);
            totalPoints.addAll(mappedPoints);
            isBeginning = false;
            if(checkIntersectionInsideWindow){
                isIntersection = true;
            }
            else{
                isIntersection = false;
            }
            return mappedPoints;
        } else {
            if(isIntersection){
                mappedPoints = applyViterbi.applyViterbi(gpsCoordinates);
                if(checkIntersectionInsideWindow || checkWindowExtention){
                    isIntersection = true;
                }
                else{
                    isIntersection = false;
                }
                if(mappedPoints != null){
                    printResult(mappedPoints);
                }
                return mappedPoints;
            }
            else{
                List <Double> ls = new ArrayList<>();
                ls.add(gpsCoordinates.get(0)[0]);
                ls.add(gpsCoordinates.get(0)[1]);
                Point tbc = mappingWithoutViterbi.mapping(ls, segmentMapping);

                if(tbc != null){
                    mappedPoints.add(tbc);
                    printResult(mappedPoints);
                    return mappedPoints;
                }
                else{
                    isIntersection = true;
                    return null;
                }
            }
        }

    }

    public static void printResult(Point mapped_point){
        StringBuilder geojson = new StringBuilder();
        geojson.append("{\n");
        geojson.append("  \"type\": \"FeatureCollection\",\n");
        geojson.append("  \"features\": [\n");

        geojson.append("    {\n");
        geojson.append("      \"type\": \"Feature\",\n");
        geojson.append("      \"geometry\": {\n");
        geojson.append("        \"type\": \"Point\",\n");
        geojson.append("        \"coordinates\": [")
                .append(mapped_point.getLongitude())
                .append(", ")
                .append(mapped_point.getLatitude())
                .append("]\n");
        geojson.append("      },\n");
        geojson.append("      \"properties\": {\n");
        geojson.append("        \"marker-color\": \"blue\",\n");
        geojson.append("        \"number\": ").append(number++).append("\n"); // Add number property
        geojson.append("      }\n");
        geojson.append("    }");

        geojson.append("\n");

        geojson.append("  ]\n");
        geojson.append("}");
        System.out.println(geojson);
    }

    public static void printResult(List<Point>result){
        StringBuilder geojson = new StringBuilder();
        geojson.append("{\n");
        geojson.append("  \"type\": \"FeatureCollection\",\n");
        geojson.append("  \"features\": [\n");

        for (int i = 0; i < result.size(); i++) {
            Point point = result.get(i);
            geojson.append("    {\n");
            geojson.append("      \"type\": \"Feature\",\n");
            geojson.append("      \"geometry\": {\n");
            geojson.append("        \"type\": \"Point\",\n");
            geojson.append("        \"coordinates\": [")
                    .append(point.getLongitude())
                    .append(", ")
                    .append(point.getLatitude())
                    .append("]\n");
            geojson.append("      },\n");
            geojson.append("      \"properties\": {\n");
            geojson.append("        \"marker-color\": \"blue\",\n");
            geojson.append("        \"number\": ").append(number++).append("\n"); // Add number property
            geojson.append("      }\n");
            geojson.append("    }");

            // Append comma if it's not the last point
            if (i < result.size() - 1) {
                geojson.append(",");
            }
            geojson.append("\n");
        }

        geojson.append("  ]\n");
        geojson.append("}");

// Print the GeoJSON formatted output
        System.out.println(geojson);
    }
}
