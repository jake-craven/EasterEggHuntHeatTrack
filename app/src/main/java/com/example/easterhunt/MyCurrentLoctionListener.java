package com.example.easterhunt;

import android.location.Location;
import android.location.LocationListener;
import android.widget.TextView;

public class MyCurrentLoctionListener implements LocationListener {

    public String myLocation;
    private TextView mTextView;

    MyCurrentLoctionListener(TextView tv) {
        this.mTextView = tv;
        this.mTextView.setText("This far");
    }

    @Override
    public void onLocationChanged(Location location) {
        location.getLatitude();
        location.getLongitude();

        mTextView.setText("Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude());

    }
}