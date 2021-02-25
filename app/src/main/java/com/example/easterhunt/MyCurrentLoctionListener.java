package com.example.easterhunt;

import android.location.Location;
import android.location.LocationListener;
import android.widget.TextView;

public class MyCurrentLoctionListener implements LocationListener {

    public String myLocation;
    private TextView mTextView;
    private HuntHint testHint = new HuntHint("Bus Station", 52.659007, -8.624353);

    MyCurrentLoctionListener(TextView tv) {
        this.mTextView = tv;
        this.mTextView.setText("This far");
    }

    @Override
    public void onLocationChanged(Location location) {
        testHint.calculateDistMetres(location.getLatitude(), location.getLongitude());
        updateTextBox();
    }

    private void updateTextBox(){
        this.mTextView.setText(this.testHint.getHint()+"\nCurrent distance to target: "+this.testHint.currentDistanceLandmark+"\nExact Distance: "+this.testHint.distance);
    }
}