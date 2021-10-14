package com.example.mysensorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.j2objc.annotations.ObjectiveCName;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    DatabaseReference database;
    FirebaseFirestore db;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        email = (EditText) findViewById(R.id.id_email_login);
        password = (EditText) findViewById(R.id.id_password_login);
    }

    public void initialize() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void testing(View view) {
        database = FirebaseDatabase.getInstance().getReference();
        /*database.child("sensors").child("modulairPm").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot data = task.getResult();
                    if(data.exists()) {
                        for(DataSnapshot ds : data.getChildren()){
                            System.out.println(ds.child("city").getValue().toString());
                        }
                    }
                }
            }
        });*/
        database.child("sensors").child("modulairPm").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println(snapshot);
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

    public void insertUser(View view, User myUser) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", myUser.getName());
        user.put("lastname", myUser.getLastname());
        user.put("email", myUser.getEmail());
        user.put("password", myUser.getPassword());
        user.put("phone", myUser.getPhone());

        DocumentReference docRef = db.collection("users").document(myUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        System.out.println("exist");
                    }else {
                        db.collection("users").document(myUser.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("added");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("not added" + e);
                            }
                        });
                    }
                }
            }
        });
    }

    public void consultUser(String email) {
        DocumentReference usersRef = db.collection("users").document(email);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        System.out.println(document.getData());
                    }else {
                        System.out.println("No exists!");
                    }
                }
            }
        });
    }

    public void deleteUser(String email) {
        DocumentReference usersRef = db.collection("users").document(email);
        usersRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("Delete");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("not deleted" + e);
            }
        });
    }

    public void updateUser(User myUser, String previousEmail) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", myUser.getName());
        user.put("lastname", myUser.getLastname());
        user.put("email", myUser.getEmail());
        user.put("password", myUser.getPassword());
        user.put("phone", myUser.getPhone());

        if(myUser.getEmail().equals(previousEmail)) {
            DocumentReference docRef = db.collection("users").document(previousEmail);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            db.collection("users").document(previousEmail).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    System.out.println("updated");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("not updated" + e);
                                }
                            });
                        }
                    }
                }
            });
        }else {
            deleteUser(previousEmail);
            DocumentReference docRef = db.collection("users").document(myUser.getEmail());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(!document.exists()) {
                            db.collection("users").document(myUser.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    System.out.println("updated");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println("not updated" + e);
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    public void login(View view) {
        if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Los campos son obligatorios", Toast.LENGTH_SHORT).show();
        }else {
            validationLogin(email.getText().toString(), password.getText().toString());
        }
    }

    public void validationLogin(String email, String password) {
        DocumentReference usersRef = db.collection("users").document(email);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        if(document.get("password").equals(password)) {
                            System.out.println(document.getData());
                            if(document.get("type").equals("cliente")) {
                                Toast.makeText(getApplicationContext(), "Bienvenido " + document.get("name").toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), panelcliente.class);
                                startActivity(intent);
                            }else if (document.get("type").equals("tecnico")) {
                                Toast.makeText(getApplicationContext(), "Bienvenido " + document.get("name").toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), paneltecnico.class);
                                startActivity(intent);
                            }else {
                                Toast.makeText(getApplicationContext(), "Bienvenido Admin!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), paneladmin.class);
                                startActivity(intent);
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Verifique su email", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Ocurrió un error!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}