package com.example.mysensorapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class User {
    private FirebaseFirestore db;
    private String name, lastname, email, phone, password;

    public User() {
    }

    public User(String name, String lastname, String email, String password, String phone) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public HashMap<String, Object> getUser(String name, String lastname, String email, String password, String phone) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("lastname", lastname);
        user.put("email", email);
        user.put("password", password);
        user.put("phone", phone);
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
