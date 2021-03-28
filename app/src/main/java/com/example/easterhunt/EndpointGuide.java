package com.example.easterhunt;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class EndpointGuide  {
    final String TAG = "EndpointGuide";

    private HuntHint[] hints;
    private double latitude;
    private double longitude;
    int currentDistanceLandmark;
    private int kilometre = 1000;
    int distance = 0;
    private LinearLayout Buttons;
    private LinearLayout radar;
    private int heatMapUpdater = 2;
    private int heatMapDist = -1;

    public EndpointGuide(double latitude, double longitude, HuntHint[] hints, LinearLayout Buttons, LinearLayout radar){
        this.Buttons = Buttons;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hints = hints;
        this.currentDistanceLandmark = 0;
        this.radar = radar;
        setup();
    }

    private void setup(){
        Log.d(TAG, "setup: Add Buttons");
        for(int index = 0; index < hints.length ; ++index) {
            Buttons.addView(hints[index]);
        }
        hints[0].unlock();
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
        Log.d(TAG, "calculateDistMetres: Distance "+dist+" metres");
        calculateLandmark(dist);
        updateHints((int) dist);
        heatMap((int) dist);
        distance = (int) dist;
    }

    private void calculateLandmark(float dist){
        int distanceModifier = 10;
        if(dist < 200){
            this.currentDistanceLandmark = distanceModifier;
            this.currentDistanceLandmark += (((int) dist) / distanceModifier) * distanceModifier ;
        }
    }

    private void updateHints(int distance){
        for(int index = 0; index < hints.length ; ++index){
            hints[index].UpdatedDistance(distance);
        }
    }

    private void heatMap(int newDistance){
        if(heatMapDist == -1){
            heatMapDist = newDistance;
            return;
        }
        if(heatMapDist - heatMapUpdater > newDistance  ) {
            setHotter();
        } else if(heatMapDist+heatMapUpdater < newDistance){
            setColder();
        } else {
            return;
        }
        heatMapDist = newDistance;
    }

    private void setColder() {
        radar.setBackgroundColor(Color.parseColor("#960000"));
    }

    private void setHotter() {
        radar.setBackgroundColor(Color.parseColor("#00FF00"));
    }
}
