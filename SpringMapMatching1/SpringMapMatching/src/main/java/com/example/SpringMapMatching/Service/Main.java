package com.example.SpringMapMatching.Service;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        // Given JSON data
        String jsonData = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.37275184885152,12.78885102455311],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.37293980715134,12.795144220252226],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.37465062440981,12.80328927452365],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.3774387276955,12.807025582901744],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.380122245059,12.80961557709287],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.38211387003076,12.811782266349766],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.38388438119546,12.813534847208714],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.38656247003274,12.81541357231913],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.38959663729878,12.816374216355428],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.39454820742998,12.817007847494068],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.39904464611885,12.817043327312831],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.40367483313116,12.817209701251855],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.40627703689444,12.81737446661596],\"type\":\"Point\"}},{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"coordinates\":[77.40810930222233,12.81813573799812],\"type\":\"Point\"}}]}";

        // Parse the JSON data
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray features = jsonObject.getJSONArray("features");

        // Initialize a 2D array to store coordinates
        double[][] coordinates = new double[features.length()][2];

        // Extract coordinates from each feature
        for (int i = 0; i < features.length(); i++) {
            JSONObject feature = features.getJSONObject(i);
            JSONObject geometry = feature.getJSONObject("geometry");
            JSONArray coordArray = geometry.getJSONArray("coordinates");
            double longitude = coordArray.getDouble(0);
            double latitude = coordArray.getDouble(1);
            coordinates[i][0] = longitude;
            coordinates[i][1] = latitude;
        }

        // Print the 2D array
        for (int i = 0; i < coordinates.length; i++) {
            System.out.println("[" + coordinates[i][0] + ", " + coordinates[i][1] + "],");
        }
    }
}
