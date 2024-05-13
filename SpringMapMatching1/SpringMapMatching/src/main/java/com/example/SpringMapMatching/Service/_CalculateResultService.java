package com.example.SpringMapMatching.Service;

import com.example.SpringMapMatching.Model.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class _CalculateResultService {
    static Map<Integer, Integer> stateToSegmentID = new HashMap<>();

    @Autowired
    private SegmentGraph segmentGraph;

    @Autowired
    private RoadGraph roadGraph;


    private static int num = 0;
    private static double gpsErrorInitial;

    public List<Point> calculateResult(double[][] viterbi, int[][] backpointer, Map<Integer, Integer> segmentToState, Point[][] closestPoints, List<Double[]> gpsCoordinates) {
        System.out.println(gpsCoordinates.size());
        System.out.println(gpsCoordinates.get(0)[0] + " " + gpsCoordinates.get(0)[1]);
        for(int k = 0; k < viterbi.length; k++){
            for(int l = 0; l < viterbi[k].length; l++){
                System.out.print(viterbi[k][l] + " ");
            }
            System.out.println("");
        }
        System.out.println(viterbi.length);

        for(int k = 0; k < viterbi.length; k++){
            for(int l = 0; l < viterbi[k].length; l++){
                System.out.print(stateToSegmentID.get(backpointer[k][l]) + " ");
            }
            System.out.println("");
        }
        double maxProb = Double.NEGATIVE_INFINITY;
        int maxState = -1;

        for (int j = 0; j < segmentToState.size(); j++) {
            if (viterbi[viterbi.length- 1][j] != 0.0 && viterbi[viterbi.length - 1][j] > maxProb) {
                maxProb = viterbi[viterbi.length- 1][j];
                maxState = j;
            }
        }

        //most_likely_path = stateToSegmentID.get(maxState);


        int[] path = new int[gpsCoordinates.size()];
        path[gpsCoordinates.size() - 1] = maxState;


        for (int t = gpsCoordinates.size() - 2; t >= 0; t--) {
            path[t] = backpointer[t + 1][path[t + 1]];
        }

//        for(int t = 0; t < path.length; t++){
//            System.out.print(stateToSegmentID.get(path[t]) + " ");
//        }
//        System.out.println("------------------------");

        for(int t = 1; t < gpsCoordinates.size(); t++){
            if(stateToSegmentID.get(path[t - 1]) != stateToSegmentID.get(path[t])){
                boolean isWindowBreak = isIntersectionWindow(stateToSegmentID.get(path[t - 1]), stateToSegmentID.get(path[t]));
                if(isWindowBreak){

                    maxProb = Double.NEGATIVE_INFINITY;
                    maxState = -1;

                    for (int k = 0; k < segmentToState.size(); k++) {
                        if (viterbi[t - 1][k] != 0.0 && viterbi[t- 1][k] > maxProb) {
                            maxProb = viterbi[t - 1][k];
                            maxState = k;
                        }
                    }

                    _NewViterbiService.most_likely_path = stateToSegmentID.get(maxState);
                    int[] new_path = new int[t];
                    new_path[t - 1] = maxState;

                    for (int x = t - 2; x >= 0; x--) {
                        new_path[x] = backpointer[x + 1][new_path[x + 1]];
                    }

                    List<Point> result = new ArrayList<>() ;
                    double error = 0.0;
                    for (int x = 0; x < t; x++) {
                        result.add(closestPoints[x][new_path[x]]);
                        if(_NewViterbiService.isBeginning){
                            error += Point.haversineDistance(new Point(gpsCoordinates.get(x)[0], gpsCoordinates.get(x)[1]), closestPoints[x][new_path[x]]);
                        }
                    }

                    if(_NewViterbiService.isBeginning){
                        gpsErrorInitial = error / (double)(t);
                    }
                    _NewViterbiService.makeWindow = t;
                    _NewViterbiService.checkIntersectionInsideWindow = true;
                    return result;
                }
                else{
                    continue;
                }
            }
        }

        List<Point> result = new ArrayList<>();

        //double error = 0.0;

        //List<Double> gpsErrorOfPaths = computeGPSError(backpointer, maxState, sw, ew, segmentToState, closestPoints, gpsCoordinates);

//        double minError = -1.0;
//        int minErrorState = -1;

//        if(sw == 0){
//            for (int t = sw; t < ew; t++) {
//                result.add(closestPoints[t - sw][path[t - sw]]);
//                error += Point.haversineDistance(new Point(gpsCoordinates.get(t)[0], gpsCoordinates.get(t)[1]), closestPoints[t - sw][path[t - sw]]);
//            }
//            _NewViterbiService.most_likely_path = stateToSegmentID.get(maxState);
//            gpsErrorInitial = error / (15.00);
//            _NewViterbiService.check = true;
//            return result;
//        }
//        else{
//            for(int k = 0; k < gpsErrorOfPaths.size(); k++){
//                double absGPSError = Math.abs(gpsErrorInitial - gpsErrorOfPaths.get(k));
//                if(absGPSError < minError){
//                    minError = absGPSError;
//                    minErrorState = k;
//                }
//            }
//
//            if((minErrorState == maxState) || (ew - sw >= 45) || ew >= gpsCoordinates.size()){
//                for (int t = sw; t < ew; t++) {
//                    result.add(closestPoints[t - sw][path[t - sw]]);
//                }
//                gpsErrorInitial = gpsErrorOfPaths.get(maxState);
//                _NewViterbiService.most_likely_path = stateToSegmentID.get(maxState);
//                _NewViterbiService.check = true;
//                return result;
//            }
//            else{
//                result = null;
//                _NewViterbiService.startWindow = sw;
//                _NewViterbiService.endWindow = ew + 15;
//                _NewViterbiService.check = false;
//                return result;
//            }
//        }

        for (int t = 0; t < path.length; t++) {
            result.add(closestPoints[t][path[t]]);
        }
        _NewViterbiService.most_likely_path = stateToSegmentID.get(maxState);

        return result;
    }

    public boolean isIntersectionWindow(int prevSegment, int currSegment){
        boolean isConnection = _ConnectionOrIntersectionService.connectionExist(prevSegment, currSegment, segmentGraph);

        if(isConnection){
            boolean isIntersection = _ConnectionOrIntersectionService.intersectionExist(prevSegment, currSegment, roadGraph);
            if(isIntersection){
                return true;
            }
        }

        Set<Integer> moreConnectedVertices = _ApplyViterbiService.getVerticesAtDistance(segmentGraph, currSegment, 4);
        if(moreConnectedVertices.contains(prevSegment)){
            return true;
        }
        return false;
    }

    public static List<Double> computeGPSError(int [][] backpointer, int maxState, int sw, int ew, Map<Integer, Integer> segmentToState, Point[][] closestPoint, List<Double[]> gpsCoordinates){

        List<Double> gpsError = new ArrayList<>();
        for(int i = 0; i < backpointer[0].length; i++){
            boolean flag = false;
            int[] path = new int[ew - sw];
            path[ew - sw - 1] = backpointer[backpointer.length - 1][i];

            for (int t = ew - sw - 2; t >= 0; t--) {
                if(path[t + 1] == -1){
                    flag = true;
                    break;
                }
                path[t] = backpointer[t + 1][path[t + 1]];
            }

            if(flag){
                gpsError.add(-1.0);
                continue;
            }

            double error = 0.0;


            for(int j = 0; j < path.length; j++){
                if(path[j] == -1){
                    break;
                }

                error += Point.haversineDistance(new Point(gpsCoordinates.get(sw + j)[0], gpsCoordinates.get(sw + j)[1]), closestPoint[j][path[j]]);
            }

            gpsError.add((error / (double)(ew - sw)));
        }
        return gpsError;
    }
}
