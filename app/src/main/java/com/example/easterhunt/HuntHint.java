package com.example.easterhunt;

public class HuntHint {

    private String []hints;
    private double latitude;
    private double longitude;
    int currentDistanceLandmark;
    private int kilometre = 1000;
    float distance = 0;
    public HuntHint(String hint, double latitude, double longitude){
        this.hints = new String[1];
        this.hints[0] = hint;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentDistanceLandmark = 0;
    }

    public HuntHint(String []hint, double latitude, double longitude){
        this.hints = hint;
        this.latitude = latitude;
        this.longitude = longitude;
        this.currentDistanceLandmark = 0;
    }

    public String getHint(){
        return hints[0];
    }

    public void calculateDistMetres(double curLatitude, double curLongitude){
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(this.latitude-curLatitude);
        double dLng = Math.toRadians(this.longitude-curLongitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(curLatitude)) * Math.cos(Math.toRadians(this.latitude)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        calculateLandmark(dist);
        distance = dist;
    }

    private void calculateLandmark(float dist){
        int distanceModifier = kilometre;
        if(dist < kilometre){
            distanceModifier = 100;
        }
        if(dist < distanceModifier){
            this.currentDistanceLandmark = distanceModifier;
        }
        else {
            this.currentDistanceLandmark = (((int) dist) / distanceModifier) * distanceModifier ;
        }
    }
}
