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

public class modulairsensor extends AppCompatActivity {

    private TextView temp, rh, pm1, pm10, pm25, date, hour, model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modulairsensor);

        temp = (TextView) findViewById(R.id.id_temperature_modul);
        rh = (TextView) findViewById(R.id.id_rh_modul);
        pm1 = (TextView) findViewById(R.id.id_pm1_modul);
        pm10 = (TextView) findViewById(R.id.id_pm10_modul);
        pm25 = (TextView) findViewById(R.id.id_pm25_modul);
        date = (TextView) findViewById(R.id.id_date_modul);
        hour = (TextView) findViewById(R.id.id_hour_modul);
        model = (TextView) findViewById(R.id.id_model_modul);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("sensors").child("modulairPm").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()) {
                    temp.setText(snapshot.child("temperature").getValue().toString());
                    rh.setText(snapshot.child("rh").getValue().toString());
                    pm1.setText(snapshot.child("pm1").getValue().toString());
                    pm10.setText(snapshot.child("pm10").getValue().toString());
                    pm25.setText(snapshot.child("pm25").getValue().toString());
                    date.setText(snapshot.child("date").getValue().toString());
                    hour.setText(snapshot.child("hour").getValue().toString());
                    model.setText(snapshot.child("model").getValue().toString());
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