package com.example.will.eateryapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}
