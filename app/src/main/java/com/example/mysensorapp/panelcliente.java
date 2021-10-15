package com.example.mysensorapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mysensorapp.databinding.ActivityPanelclienteBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class panelcliente extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPanelclienteBinding binding;
    private TextView nombreText, emailText, tipoText;
    String nombre, email, tipo;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPanelclienteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setSupportActionBar(binding.appBarPanelcliente.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_contact, R.id.nav_about)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_panelcliente);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        nombreText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.id_nombre);
        emailText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.id_correo);
        tipoText = (TextView) navigationView.getHeaderView(0).findViewById(R.id.id_tipo_usuario);

        intent = getIntent();
        nombre = intent.getStringExtra("nombre");
        email = intent.getStringExtra("email");
        tipo = intent.getStringExtra("tipo");
        nombreText.setText(nombre);
        emailText.setText(email);
        tipoText.setText(tipo);

        TextView cerdo = (TextView) findViewById(R.id.nav_home);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.panelcliente, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_panelcliente);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public boolean onMenuItemClickMethod(MenuItem menuItem) {
        if(tipo.equals("cliente")) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
        }else if(tipo.equals("tecnico")) {
            Intent i = new Intent(this, paneltecnico.class);
            i.putExtra("nombre",nombre );
            i.putExtra("email", email);
            i.putExtra("tipo", tipo);
            startActivity(i);
            return true;
        }else if(tipo.equals("admin")){
            Intent i = new Intent(this, paneladmin.class);
            i.putExtra("nombre",nombre );
            i.putExtra("email", email);
            i.putExtra("tipo", tipo);
            startActivity(i);
            return true;
        }
        return true;
    }


}