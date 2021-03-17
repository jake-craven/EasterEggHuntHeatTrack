package com.example.easterhunt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    MyCurrentLoctionListener CurLoc;
    LocationManager locationManager;
    private final int locationRequestCode = 101;
    ImageView Radar;
    TextView distanceText;
    TextView HintText;
    LinearLayout Buttons;
    LinearLayout UIBackground;
    int locationIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent mIntent = getIntent();
        locationIndex = mIntent.getIntExtra("index", 0);
        initialiseUI();
        try {
            EndpointGuide endpoint = readEndpoint(locationIndex);
            CurLoc = new MyCurrentLoctionListener(distanceText);
            CurLoc.setEndpoint(endpoint);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            permissionDependencies();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initialiseUI(){
        Buttons = findViewById(R.id.HintButtons);
        distanceText = findViewById(R.id.DistanceText);
        HintText = findViewById(R.id.HintTextBox);
        Radar = findViewById(R.id.RadarImage);
        UIBackground = findViewById(R.id.UIBackground);

        Animation rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        Radar.startAnimation(rotation);

    }


    // And override this method
    @Override
    public boolean onNavigateUp() {
//        finish();
        return true;
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

    private EndpointGuide readEndpoint(int index) throws JSONException {
        JSONObject locations = loadJSONFromAsset();
        Log.d(TAG, "readEndpoint: "+locations.toString());
        if (locations != null) {
            JSONObject location = (JSONObject) locations.getJSONArray("locations").getJSONObject(index);
            Log.d(TAG, "readEndpoint: "+location.toString());
            double longitude = location.getDouble("longitude");
            double latitude = location.getDouble("latitude");
            JSONArray hints = location.getJSONArray("hints");
            HuntHint huntHintsArr[] = new HuntHint[hints.length()];
            for (int hintIndex = 0; hintIndex < hints.length(); hintIndex++){
                Log.d(TAG, "readEndpoint: Array size "+hints.length() + "  index "+hintIndex);
                JSONObject jsonHint = hints.getJSONObject(hintIndex);
                Log.d(TAG, "readEndpoint: "+jsonHint.toString());
                HuntHint huntHint = new HuntHint(this.getApplicationContext(),
                        jsonHint.getString("hint"),
                        jsonHint.getInt("unlockDistance"),
                        HintText,
                        hintIndex);
                huntHintsArr[hintIndex] = huntHint;
            }
            EndpointGuide endpointGuide = new EndpointGuide(latitude, longitude, huntHintsArr, Buttons, UIBackground);
            return endpointGuide;
        }
        return null;
    }

    private JSONObject loadJSONFromAsset() {
        JSONObject json;
        try {
            InputStream is = this.getApplicationContext().getAssets().open("locations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}