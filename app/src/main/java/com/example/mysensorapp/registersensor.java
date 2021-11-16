package com.example.mysensorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class registersensor extends AppCompatActivity {
    FirebaseFirestore db;
    Intent intent;
    private EditText idMac;
    private Spinner type;
    String email, typeUser, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registersensor);

        idMac = (EditText) findViewById(R.id.idmac);
        type = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeSensors, android.R.layout.simple_spinner_item);
        type.setAdapter(adapter);

        intent = getIntent();
        name = intent.getStringExtra("nombre");
        typeUser = intent.getStringExtra("tipo");
        email = intent.getStringExtra("email");

        initialize();
    }

    public void initialize() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void btnAddSensor(View view) {
        String mac = idMac.getText().toString();
        String typeSensor = type.getSelectedItem().toString();
        if(mac.isEmpty()) {
            Toast.makeText(this, "Digite la dirección MAC del sensor", Toast.LENGTH_SHORT).show();
        }else {
            newSensor(mac, typeSensor);
        }
    }

    public void newSensor(String mac, String typeSensor) {
        HashMap<String, String> sensor = new HashMap<>();
        sensor.put("macAddress", mac);
        sensor.put("type", typeSensor);

        DocumentReference ref = db.collection("sensorsLowCost").document(mac);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        Toast.makeText(getApplicationContext(), "La dirección MAC se encuentra registrada", Toast.LENGTH_SHORT).show();
                    }else {
                        db.collection("sensorsLowCost").document(mac).set(sensor).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Sensor añadido", Toast.LENGTH_SHORT).show();
                                idMac.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Error sensor" + e);
                                Toast.makeText(getApplicationContext(), "Ocurrió un error al añadir el nuevo sensor", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }

    public void backToTecnical(View view) {
        Intent i = new Intent(this, paneltecnico.class);
        i.putExtra("nombre",name );
        i.putExtra("email", email);
        i.putExtra("tipo", typeUser);
        startActivity(i);
    }
}