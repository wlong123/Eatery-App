package com.example.will.eateryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.util.Log;
import android.location.Location;
import android.Manifest.permission;


public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
/*
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main, container, false);
        final Button b = v.findViewById(R.id.create_group);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "To Delete a List, Hold Down on the Button",
                        Toast.LENGTH_SHORT).show();
            }

        });

        return v;
    }
    */

    public void onClick(View v){
        Toast.makeText(getApplicationContext(), "To Delete a List, Hold Down on the Button",
                Toast.LENGTH_SHORT).show();
        getLastLocation();
    }

    public void getLastLocation(){
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Toast.makeText(getApplicationContext(), "failed",
                                        Toast.LENGTH_SHORT).show();
                                // Logic to handle location object
                                //Log.d(location.toString(),"gotLocation");
                                Toast.makeText(getApplicationContext(), location.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch(SecurityException e){
            Log.d("Failed","User Permission not Enabled");
        }

    }

}
