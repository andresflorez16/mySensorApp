package com.example.mysensorapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;

public class mhzsensor extends AppCompatActivity {

    private TextView temp, date, rh, co2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhzsensor);
        temp = (TextView) findViewById(R.id.id_temp_mhz);
        rh = (TextView) findViewById(R.id.id_rh_mhz);
        date = (TextView) findViewById(R.id.id_date_mhz);
        co2 = (TextView) findViewById(R.id.id_co2_mhz);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

        myRef.child("sensoresbajocosto").child("mhz19c").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    temp.setText(task.getResult().child("temperatura").getValue().toString());
                    rh.setText(task.getResult().child("humedad").getValue().toString());
                    date.setText(task.getResult().child("fecha").getValue().toString());
                    co2.setText(task.getResult().child("co2").getValue().toString());
                }
            }
        });

    }
}