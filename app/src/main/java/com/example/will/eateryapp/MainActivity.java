package com.example.will.eateryapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.support.design.widget.Snackbar;



public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    private static final int REQUEST_FINE_LOCATION = 100;
    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CONTACTS = 1;

    private View mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (myToolbar != null)
        {
            setSupportActionBar(myToolbar);//To display toolbar

            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setElevation(0); // or other...
        }
        initializeEateryLoc();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLayout = findViewById(R.id.main_layout);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_group:
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void initializeEateryLoc () {
        String diningStr = "";
        try {
            URL url = new URL("https://now.dining.cornell.edu/api/1.0/dining/eateries.json");
            /*this url will give us 28/34 eateries with terrace, macs, temple of zeus, gimme coffee,
            mandible, and forg and gavel missing */
            ReadURLTask task = new ReadURLTask();
            task.execute(url);
            diningStr = task.get();
            Log.d("JSON",diningStr);
            Log.d("JSON",""+diningStr.length());
        }
        catch (Exception e){
            Log.d("JSON",e.getMessage());
            return;
        }
        try {
            JSONObject diningJson = new JSONObject(diningStr);
            JSONObject data = diningJson.getJSONObject("data");
            JSONArray eateries = data.getJSONArray("eateries");
            Log.d("JSON","# of eateries ="+eateries.length());
            for(int i=0; i<eateries.length(); i++){
                Log.d("JSON", eateries.getJSONObject(i).getString("slug"));
            }
        }
        catch (JSONException e){
            Log.d("JSON", e.getMessage());
        }

    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private class ReadURLTask extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... urls) {
            int count = urls.length;
            HttpURLConnection urlConnection = null;
            /*long totalSize = 0;
            for (int i = 0; i < count; i++) {
                totalSize += Downloader.downloadFile(urls[i]);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;*/
            try {
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                Log.d("url", urlConnection.toString());
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                StringBuilder sb = new StringBuilder();
                BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
                for (String line = r.readLine(); line != null; line = r.readLine()) {
                    sb.append(line);
                }
                in.close();
                urlConnection.disconnect();
                return sb.toString();
            } catch (IOException e) {
                Log.d("url", "IO esception");
                return "";
            }

        }
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
        requestLocationPermission();
        //getLastLocation();
    }

    public void getLastLocation(){
        Log.d("success", "success");
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            //Log.d("success", location.toString());
                            if (location != null) {
                                //Toast.makeText(getApplicationContext(), "failed",
                                  //      Toast.LENGTH_SHORT).show();
                                // Logic to handle location object
                                //Log.d(location.toString(),"gotLocation");
                                Toast.makeText(getApplicationContext(), location.
                                                getLatitude() + ", " + location.getLongitude(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch(SecurityException e){
            Log.d("Failed","User Permission not Enabled");
        }

    }

/*
    public void requestLocationPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                android.support.design.widget.Snackbar.make(mLayout, R.string.permission_camera_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA);
                            }
                        })
                        .show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        REQUEST_FINE_LOCATION);

                // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
*/

    public void requestLocationPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                    Snackbar.make(mLayout, R.string.permission_location_rationale,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{permission.ACCESS_FINE_LOCATION},
                                        REQUEST_FINE_LOCATION);
                            }
                        })
                        .show();


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION);

                // MY_PERMISSION_REQUEST_READ_FINE_LOCATION is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        getLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the contacts-related task you need to do.
                    getLastLocation();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}


