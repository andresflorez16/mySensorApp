package com.example.mysensorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class paneladmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneladmin);
    }

    public void crudUsers(View view) {
        Intent intent = new Intent(this, crudusuarios.class);
        startActivity(intent);
    }

    public void panelClient(View view) {
        Intent intent = new Intent(this, panelcliente.class);
        startActivity(intent);
    }

    public void panelTecnico(View view) {
        Intent intent = new Intent(this, paneltecnico.class);
        startActivity(intent);
    }
}