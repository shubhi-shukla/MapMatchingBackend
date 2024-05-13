package com.example.SpringMapMatching.Model;

import org.geojson.GeoJsonObject;

import java.util.Objects;


public class Point extends GeoJsonObject {
    Double longitude ;
    Double latitude ;

    public Point(Double longitude , Double latitude){
        this.longitude = longitude ;
        this.latitude = latitude ;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Point other = (Point) obj;
        return Double.doubleToLongBits(other.getLongitude()) == Double.doubleToLongBits(longitude) && Double.doubleToLongBits(other.getLatitude()) == Double.doubleToLongBits(latitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude);
    }

    public String toString() {
        return "Latitude: " + latitude + ", Longitude: " + longitude;
    }

    public static double toRadians(double degrees) {
        return degrees * Math.PI / 180;
    }


    public static double haversineDistance(Point p1, Point p2) {
//        double lat1 = 0.0;
//        double lon1 = 0.0;
//        if (p1 != null) {
//            lat1 = toRadians(p1.latitude);
//            lon1 = toRadians(p2.longitude);
//        }else{
//            System.out.println("NUll");
//        }
//
//        double lat2 = 0.0;
//        double lon2 = 0.0;
//        if (p2 != null) {
//            lat2 = toRadians(p2.latitude);
//            lon2 = toRadians(p2.longitude);
//        } else {
//            System.out.println("NULL");
//        }
        double lat1 = toRadians(p1.latitude);
        double lon1 = toRadians(p1.longitude);
        double lat2 = toRadians(p2.latitude);
        double lon2 = toRadians(p2.longitude);


        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.pow(Math.sin(dLon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Radius of the Earth in kilometers
        double R = 6371.0;

        return R * c;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

//    public static double haversineDistance(Point p1, Point p2) {
//        double lat1 = toRadians(p1.latitude);
//        double lon1 = toRadians(p1.longitude);
//        double lat2 = toRadians(p2.latitude);
//        double lon2 = toRadians(p2.longitude);
//
//        double dLat = lat2 - lat1;
//        double dLon = lon2 - lon1;
//
//        double a = Math.pow(Math.sin(dLat / 2), 2) +
//                Math.cos(lat1) * Math.cos(lat2) *
//                        Math.pow(Math.sin(dLon / 2), 2);
//
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//
//        // Radius of the Earth in kilometers
//        double R = 6371.0;
//
//        return R * c;
//    }

}

