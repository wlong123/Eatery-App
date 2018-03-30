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

public class MainActivity extends AppCompatActivity {

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
            }
            catch(IOException e){
                Log.d("url", "IO esception");
                return "";
            }

        }

    }

}
