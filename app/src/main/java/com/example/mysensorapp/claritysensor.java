package com.example.mysensorapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class claritysensor extends AppCompatActivity {
    private TextView temp, rh, no2, pm1Mass, pm1Num, pm10Mass,pm10Num, pm25Mass, pm25Num, date, hour, model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claritysensor);

        temp = (TextView) findViewById(R.id.id_temp_clarity);
        rh = (TextView) findViewById(R.id.id_rh_clarity);
        pm1Mass = (TextView) findViewById(R.id.id_pm1Mass_clarity);
        pm1Num = (TextView) findViewById(R.id.id_pm1Num_clarity);
        pm10Mass = (TextView) findViewById(R.id.id_pm10Mass_clarity);
        pm10Num = (TextView) findViewById(R.id.id_pm10Num_clarity);
        pm25Mass = (TextView) findViewById(R.id.id_pm25Mass_clarity);
        pm25Num = (TextView) findViewById(R.id.id_pm25Num_clarity);
        date = (TextView) findViewById(R.id.id_date_clarity);
        hour = (TextView) findViewById(R.id.id_hour_clarity);
        no2 = (TextView) findViewById(R.id.id_no2_clarity);
        model = (TextView) findViewById(R.id.id_model_clarity);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("sensors").child("clarity").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()) {
                    temp.setText(snapshot.child("temperature").getValue().toString());
                    rh.setText(snapshot.child("rh").getValue().toString());
                    pm10Mass.setText(snapshot.child("pm10Mass").getValue().toString());
                    pm10Num.setText(snapshot.child("pm10Num").getValue().toString());
                    pm1Mass.setText(snapshot.child("pm1Mass").getValue().toString());
                    pm1Num.setText(snapshot.child("pm1Num").getValue().toString());
                    pm25Mass.setText(snapshot.child("pm2_5Mass").getValue().toString());
                    pm25Num.setText(snapshot.child("pm2_5Num").getValue().toString());
                    date.setText(snapshot.child("date").getValue().toString());
                    hour.setText(snapshot.child("hour").getValue().toString());
                    model.setText(snapshot.child("model").getValue().toString());
                    no2.setText(snapshot.child("NO2").getValue().toString());
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}