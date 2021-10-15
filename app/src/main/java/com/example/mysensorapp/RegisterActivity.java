package com.example.mysensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    FirebaseFirestore db;
    private EditText nameText, lastnameText, emailText, passwordText, phoneText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = (EditText) findViewById(R.id.id_nombre_register);
        lastnameText = (EditText) findViewById(R.id.id_apellido_register);
        emailText = (EditText) findViewById(R.id.id_email_register);
        passwordText = (EditText) findViewById(R.id.id_password_register);
        phoneText = (EditText) findViewById(R.id.id_phone_register);

        initialize();
    }

    public void initialize() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void btnRegister(View view) {
        String name = nameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String phone = phoneText.getText().toString();

        if(name.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Los campos son obligatorios!", Toast.LENGTH_SHORT).show();
        }else {
            User user = new User(name, lastname, email, password, phone, "cliente");
            insertUser(view, user);
        }
    }

    public void insertUser(View view, User myUser) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", myUser.getName());
        user.put("lastname", myUser.getLastname());
        user.put("email", myUser.getEmail());
        user.put("password", myUser.getPassword());
        user.put("phone", myUser.getPhone());
        user.put("type", myUser.getType());

        DocumentReference docRef = db.collection("users").document(myUser.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {
                        Toast.makeText(getApplicationContext(), "El email ya está registrado", Toast.LENGTH_SHORT).show();
                    }else {
                        db.collection("users").document(myUser.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Usuario añadido", Toast.LENGTH_SHORT).show();
                                Clear(view);
                                Intent intent = new Intent(getApplicationContext(), panelcliente.class);
                                intent.putExtra("nombre", myUser.getName());
                                intent.putExtra("email", myUser.getEmail());
                                intent.putExtra("tipo", "cliente");
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("not added" + e);
                                Toast.makeText(getApplicationContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    public void Clear(View view) {
        nameText.setText("");
        lastnameText.setText("");
        emailText.setText("");
        passwordText.setText("");
        phoneText.setText("");
    }

    public void backToMain(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}