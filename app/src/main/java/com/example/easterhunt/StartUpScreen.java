package com.example.easterhunt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class StartUpScreen extends AppCompatActivity {

    private static final String TAG = "StartUp";
    LinearLayout layout;
    Button buttons[];
    private final int locationRequestCode = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        initialiseUI();
        readJSONMakeButtons();
        permissionDependencies();
    }

    private void initialiseUI(){
        layout = findViewById(R.id.layout);
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
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        readJSONMakeButtons();
//    }

    private void ButtonSetup(Button button, int index){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MATCH_PARENT,
                WRAP_CONTENT);
        lp.setMargins(10,10,10,10);
        button.setLayoutParams(lp);
        button.setText("Show Location "+(index+1));
        button.setVisibility(VISIBLE);
        button.setBackgroundColor(Color.DKGRAY);
        GradientDrawable shape =  new GradientDrawable();
        shape.setColor(Color.GRAY);
        shape.setCornerRadius( 8 );
        button.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        button.setTextSize(18);
        button.setMinimumHeight(50);
        button.setPadding(20,20,20,20);
        button.setBackground(shape);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextScreen(index);
            }
        });
        layout.addView(button);
    }

    private void nextScreen(int index) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    private void readJSONMakeButtons() {
        JSONObject json;
        try {
            InputStream is = this.getApplicationContext().getAssets().open("UL.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new JSONObject(new String(buffer, "UTF-8"));
            JSONArray locations = json.getJSONArray("locations");
            buttons = new Button[locations.length()];
            for(int index = 0; index <buttons.length ; ++index){
                Log.d(TAG, "readJSONMakeButtons: Making buton num "+index);
                buttons[index] = new Button(this.getApplicationContext());
                ButtonSetup(buttons[index], index);
            }
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        }
    }
}