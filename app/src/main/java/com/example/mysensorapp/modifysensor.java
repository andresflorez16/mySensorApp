package com.example.mysensorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class modifysensor extends AppCompatActivity {

    FirebaseFirestore db;
    private EditText idMac;
    private RadioGroup rGroup;
    private RadioButton rButton;
    private Spinner typeSensor;
    Intent i;
    String nombre, email, tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifysensor);

        idMac = (EditText) findViewById(R.id.idmac2);
        typeSensor = (Spinner) findViewById(R.id.spinner3);
        rGroup = (RadioGroup) findViewById(R.id.radioGroup2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeSensors, android.R.layout.simple_spinner_item);
        typeSensor.setAdapter(adapter);

        i = getIntent();
        nombre = i.getStringExtra("nombre");
        email = i.getStringExtra("email");
        tipo = i.getStringExtra("tipo");

        initialize();
    }

    public void initialize() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void btnCrud(View view) {
        String mac = idMac.getText().toString();
        String type = typeSensor.getSelectedItem().toString();
        int radioId = rGroup.getCheckedRadioButtonId();
        rButton = findViewById(radioId);

        if(mac.isEmpty()) {
            Toast.makeText(this, "Digite la dirección MAC del sensor", Toast.LENGTH_SHORT).show();
        }else {
            switch (rButton.getText().toString()) {
                case "Registro":
                    newSensor(mac, type);
                    clear();
                    break;
                case "Buscar":
                    findSensor(mac);
                    break;
                case "Modificar":
                    updateSensor(mac, type);
                    idMac.setText("");
                    clear();
                    break;
                case "Eliminar":
                    deleteSensor(mac);
                    clear();
                    break;
            }
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

    public void updateSensor(String mac, String type) {
        HashMap<String, String> sensor = new HashMap<>();
        sensor.put("macAddress", mac);
        sensor.put("type", type);

        DocumentReference ref = db.collection("sensorsLowCost").document(mac);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        db.collection("sensorsLowCost").document(mac).set(sensor).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Sensor modificado", Toast.LENGTH_SHORT).show();
                                idMac.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Error sensor" + e);
                                Toast.makeText(getApplicationContext(), "Ocurrió un error al modificar el sensor", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "La dirección MAC no se encuentra registrada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void findSensor(String mac) {
        DocumentReference ref = db.collection("sensorsLowCost").document(mac);
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        idMac.setText(doc.get("macAddress").toString());
                        if(doc.get("type").toString().equals("MHZ19C")) {
                            typeSensor.setSelection(0);
                        }else {
                            typeSensor.setSelection(1);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "El sensor no se encuentra registrada", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void deleteSensor(String mac) {
        DocumentReference Ref = db.collection("sensorsLowCost").document(mac);
        Ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Sensor eliminado", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("not deleted" + e);
            }
        });
    }

    public void backSensor(View view) {
        Intent intent = new Intent(this, paneladmin.class);
        intent.putExtra("nombre",nombre );
        intent.putExtra("email", email);
        intent.putExtra("tipo", tipo);
        startActivity(intent);
    }

    public void clear() {
        idMac.setText("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.typeSensors, android.R.layout.simple_spinner_item);
        typeSensor.setAdapter(adapter);
    }
}