package com.example.easterhunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    MyCurrentLoctionListener CurLoc;
    LocationManager locationManager;
    private final int locationRequestCode = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView TestDialog = findViewById(R.id.testTextBox);
        CurLoc = new MyCurrentLoctionListener(TestDialog);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        permissionDependencies();
    }

    private void requestAllPermisions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                locationRequestCode);
    }

    private void permissionDependencies() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestAllPermisions();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, CurLoc);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if(requestCode == locationRequestCode){
            for(int result : grantResults){
                if(result == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this.getApplicationContext(),getString(R.string.permissions_explained),Toast.LENGTH_LONG).show();
                    
                    return;
                }
            }
            permissionDependencies();
        }
    }
}