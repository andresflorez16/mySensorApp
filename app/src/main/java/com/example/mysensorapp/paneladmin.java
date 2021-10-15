package com.example.mysensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class paneladmin extends AppCompatActivity {
    String name, email, type;
    private TextView nombreText;
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneladmin);
        nombreText = (TextView) findViewById(R.id.id_nombre_admin);
        i = getIntent();
        name = i.getStringExtra("nombre");
        email = i.getStringExtra("email");
        type = i.getStringExtra("tipo");
        nombreText.setText("Bienvenido " + name);

    }

    public void crudUsers(View view) {
        Intent intent = new Intent(this, crudusuarios.class);
        intent.putExtra("nombre", name);
        intent.putExtra("email", email);
        intent.putExtra("tipo", type);
        startActivity(intent);
    }

    public void panelClient(View view) {
        Intent intent = new Intent(this, panelcliente.class);
        intent.putExtra("nombre", name);
        intent.putExtra("email", email);
        intent.putExtra("tipo", type);
        startActivity(intent);
    }

    public void panelTecnico(View view) {
        Intent intent = new Intent(this, paneltecnico.class);
        intent.putExtra("nombre", name);
        intent.putExtra("email", email);
        intent.putExtra("tipo", type);
        startActivity(intent);
    }
}