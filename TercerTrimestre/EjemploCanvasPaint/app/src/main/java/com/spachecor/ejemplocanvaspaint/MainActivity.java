package com.spachecor.ejemplocanvaspaint;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EjemploTerceroDibujarDedoView view = findViewById(R.id.drawView);
        Button eliminarButton = findViewById(R.id.eliminarButton);
        eliminarButton.setOnClickListener(v -> {
            view.setEliminar();
            if(view.isEliminar()) eliminarButton.setText("Pintar");
            else eliminarButton.setText("Borrar");
        });
    }
}