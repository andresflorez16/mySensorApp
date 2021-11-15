package com.example.mysensorapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mysensorapp.R;
import com.example.mysensorapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    View vista;
    ImageButton btnClarity, btnModulair, btnMhz;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        vista = binding.getRoot();
        
        btnClarity = (ImageButton) vista.findViewById(R.id.id_buttonClarity);
        btnModulair = (ImageButton) vista.findViewById(R.id.id_buttonModulair);
        btnMhz = (ImageButton) vista.findViewById(R.id.id_buttonMhz);

        btnClarity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), com.example.mysensorapp.claritysensor.class);
                startActivity(i);
            }
        });
        btnModulair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), com.example.mysensorapp.modulairsensor.class);
                startActivity(i);
            }
        });
        btnMhz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), com.example.mysensorapp.mhzsensor.class);
                startActivity(i);
            }
        });


        return vista;



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}