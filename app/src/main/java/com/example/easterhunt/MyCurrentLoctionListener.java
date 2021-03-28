package com.example.easterhunt;

import android.location.Location;
import android.location.LocationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCurrentLoctionListener implements LocationListener {

    public String myLocation;
    private TextView mTextView;
    private EndpointGuide endpoint;

    MyCurrentLoctionListener(TextView tv) {
        this.mTextView = tv;
//        this.mTextView.setText("This far");
    }
    MyCurrentLoctionListener(TextView tv, EndpointGuide endpoint) {
        this.mTextView = tv;
//        this.mTextView.setText("This far");
        this.endpoint = endpoint;
    }

    public void setEndpoint(EndpointGuide endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void onLocationChanged(Location location) {
        endpoint.calculateDistMetres(location.getLatitude(), location.getLongitude());
        updateTextBox(location);
    }

    private void updateTextBox(Location location){
//        this.mTextView.setText("Current distance to target aporximately: "+this.endpoint.currentDistanceLandmark+" metres\nExact Distance: "+this.endpoint.distance+"\nLongitude: "+ location.getLongitude()+"      Latitude: "+location.getLatitude());
    }
}