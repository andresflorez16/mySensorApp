package com.example.mysensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    private RadioButton rbtnRegister, rbtnFind, rbtnDelete, rbtnModify, radioButton;
    private RadioGroup rGroup;
    private Spinner typeUsers;
    Intent i;
    String nombre, email, tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crudusuarios);

        nameText = (EditText) findViewById(R.id.id_nombre_crud);
        lastnameText = (EditText) findViewById(R.id.id_apellido_crud);
        emailText = (EditText) findViewById(R.id.id_email_crud);
        passwordText = (EditText) findViewById(R.id.id_password_crud);
        phoneText = (EditText) findViewById(R.id.id_phone_crud);

        rGroup = (RadioGroup) findViewById(R.id.radioGroup);

        rbtnRegister = (RadioButton) findViewById(R.id.rButton_register);
        rbtnFind = (RadioButton) findViewById(R.id.rButtonFind);
        rbtnDelete = (RadioButton) findViewById(R.id.rButtonDelete);
        rbtnModify = (RadioButton) findViewById(R.id.rButtonModify);

        typeUsers = (Spinner) findViewById(R.id.spinner_crud);
        ArrayAdapter<CharSequence> adapterType = ArrayAdapter.createFromResource(this, R.array.typeUsers, android.R.layout.simple_spinner_item);
        typeUsers.setAdapter(adapterType);

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

    public void selectButton(View view) {
        int radioId = rGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        System.out.println(radioButton.getText().toString());
    }

    public void btnContinuar(View view) {
        String name = nameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String phone = phoneText.getText().toString();
        String type = typeUsers.getSelectedItem().toString();
        int radioId = rGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        /*!name.isEmpty() || !lastname.isEmpty() || !email.isEmpty() || !password.isEmpty() || !phone.isEmpty()*/
        User user = new User(name, lastname, email, password, phone, type);
        if(!email.isEmpty()) {
            boolean b = !name.isEmpty() && !lastname.isEmpty() && !password.isEmpty() && !phone.isEmpty();
            switch (radioButton.getText().toString()) {
                case "Registro":
                    if(b) {
                        insertUser(view, user);
                    }else {
                        Toast.makeText(this, "Verifique los campos", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Modificar":
                    if(b) {
                        updateUser(view, user);
                    }else {
                        Toast.makeText(this, "Verifique los campos", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case "Buscar":
                    consultUser(view, email);
                    break;
                case "Eliminar":
                    deleteUser(view, email);
                    break;
            }
        }else {
            Toast.makeText(this, "Verifique el campo email y seleccione un opción", Toast.LENGTH_SHORT).show();
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
                                Clear();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
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
                Clear();
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
                        nameText.setText(document.get("name").toString());
                        lastnameText.setText(document.get("lastname").toString());
                        passwordText.setText(document.get("password").toString());
                        phoneText.setText(document.get("phone").toString());
                        if(document.get("type").toString().equals("cliente")) {
                            typeUsers.setSelection(0);
                        }else {
                            typeUsers.setSelection(1);
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "El usuario no existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void updateUser(View view, User myUser) {
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
                        db.collection("users").document(myUser.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show();
                                Clear();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Ocurrió un error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Verifique el email", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void back(View view) {
        Intent intent = new Intent(this, paneladmin.class);
        intent.putExtra("nombre",nombre );
        intent.putExtra("email", email);
        intent.putExtra("tipo", tipo);
        startActivity(intent);
    }

    public void Clear() {
        nameText.setText("");
        lastnameText.setText("");
        emailText.setText("");
        passwordText.setText("");
        phoneText.setText("");
        typeUsers.setSelection(0);
    }


}