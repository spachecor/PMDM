package com.spachecor.sumabundleintent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById(R.id.buttonSumar).setOnClickListener(v->{
            EditText editTextNumeroUno = findViewById(R.id.editTextNumberNumeroUno);
            EditText editTextNumeroDos = findViewById(R.id.editTextNumberNumeroDos);
            int numeroUno = Integer.parseInt(editTextNumeroUno.getText().toString());
            int numeroDos = Integer.parseInt(editTextNumeroDos.getText().toString());
            Intent intent = new Intent(MainActivity.this, SumaActivity.class);
            int resultado = Integer.parseInt(editTextNumeroUno.getText().toString())+Integer.parseInt(editTextNumeroDos.getText().toString());
            intent.putExtra("resultado", resultado);
            System.out.println(Integer.parseInt(editTextNumeroUno.getText().toString()));
            System.out.println(Integer.parseInt(editTextNumeroDos.getText().toString()));
            startActivity(intent);
        });
    }
}