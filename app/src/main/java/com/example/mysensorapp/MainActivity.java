package com.example.mysensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void initialize() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void insertUser(User myUser) {
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

    public void validationLogin(String email, String password) {
        DocumentReference usersRef = db.collection("users").document(email);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        if(document.get("password").equals(password)) {
                            System.out.println("WELCOME " + document.get("name"));
                        }else {
                            System.out.println("Incorrect password!");
                        }
                    }else {
                        System.out.println("Verify your email");
                    }
                }
            }
        });
    }

}