package com.spachecor.ejemplos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TerceraActivity extends AppCompatActivity {
    private static final String TAG = "TerceraActivity";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tres);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button botonVolver = findViewById(R.id.buttonVolver);
        botonVolver.setOnClickListener(v ->{
            Intent intent = new Intent(TerceraActivity.this, MainActivity.class);
            startActivity(intent);
        });
        Button atrasButton = findViewById(R.id.buttonAtras);
        atrasButton.setOnClickListener(v->{
            finish();
        });
        Button salirButton = findViewById(R.id.buttonAtras);
        salirButton.setOnClickListener(v->{
            moveTaskToBack(true);//nos mueve la app a segundo plano, como volver al escritorio.
        });
        Log.d(TAG, "onCreate: La actividad se está creando.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: La actividad está visible pero no interactiva.");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: La actividad está visible e interactiva.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: La actividad está parcialmente visible (por ejemplo, al abrir otra actividad).");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: La actividad ya no está visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: La actividad se está destruyendo.");
    }
}
