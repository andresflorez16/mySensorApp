package com.example.mysensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class crudusuarios extends AppCompatActivity {

    FirebaseFirestore db;
    private EditText nameText, lastnameText, emailText, passwordText, phoneText;
    private RadioButton rbtnRegister, rbtnFind, rbtnDelete, rbtnModify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudusuarios);

        nameText = (EditText) findViewById(R.id.id_nombre_crud);
        lastnameText = (EditText) findViewById(R.id.id_apellido_crud);
        emailText = (EditText) findViewById(R.id.id_email_crud);
        passwordText = (EditText) findViewById(R.id.id_password_crud);
        phoneText = (EditText) findViewById(R.id.id_phone_crud);

        rbtnRegister = (RadioButton) findViewById(R.id.rButton_register);
        rbtnFind = (RadioButton) findViewById(R.id.rButtonFind);
        rbtnDelete = (RadioButton) findViewById(R.id.rButtonDelete);
        rbtnModify = (RadioButton) findViewById(R.id.rButtonModify);

        initialize();

    }

    public void initialize() {
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
    }

    public void btnContinuar(View view) {
        String name = nameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String phone = phoneText.getText().toString();

        /*if(name.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            User user = new User(name, lastname, email, password, phone);
            switch (view.getId()) {
                case R.id.rButton_register:
                    if(rbtnRegister.isChecked()) {
                        insertUser(view, user);
                    }
                    break;
                case R.id.rButtonDelete:
                    if(rbtnDelete.isChecked()) {
                        deleteUser(view, email);
                    }
                    break;
                case R.id.rButtonFind:
                    if(rbtnFind.isChecked()) {
                        consultUser(view, email);
                    }
                    break;
            }
        }*/
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
                        Toast.makeText(getApplicationContext(), "El email ya está registrado", Toast.LENGTH_SHORT).show();
                    }else {
                        db.collection("users").document(myUser.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println("added");
                                Toast.makeText(getApplicationContext(), "Usuario añadido", Toast.LENGTH_SHORT).show();
                                Clear(view);
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

    public void deleteUser(View view, String email) {
        DocumentReference usersRef = db.collection("users").document(email);
        usersRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
                Clear(view);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("not deleted" + e);
            }
        });
    }

    public void consultUser(View view, String email) {
        DocumentReference usersRef = db.collection("users").document(email);
        usersRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {

                    }else {
                        System.out.println("No exists!");
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


}