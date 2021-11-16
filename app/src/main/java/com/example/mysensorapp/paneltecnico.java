package com.example.mysensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class paneltecnico extends AppCompatActivity {
    Intent intent;
    String nombre, email, tipo;
    private TextView nombreText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneltecnico);

        nombreText = (TextView) findViewById(R.id.id_nombre_tecnico);
        intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        tipo = intent.getStringExtra("tipo");
        email = intent.getStringExtra("email");
        nombreText.setText("Bienvenido " + nombre);
    }

    public void toClient(View view) {
        Intent i = new Intent(this, panelcliente.class);
        i.putExtra("nombre",nombre );
        i.putExtra("email", email);
        i.putExtra("tipo", tipo);
        startActivity(i);
    }

    public void addSensor(View view) {
        Intent i = new Intent(this, registersensor.class);
        i.putExtra("nombre",nombre );
        i.putExtra("email", email);
        i.putExtra("tipo", tipo);
        startActivity(i);
    }

    public void backToMainT(View view) {
        if(tipo.equals("tecnico")) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }else {
            Intent i = new Intent(this, paneladmin.class);
            i.putExtra("nombre",nombre );
            i.putExtra("email", email);
            i.putExtra("tipo", tipo);
            startActivity(i);
        }
    }

}