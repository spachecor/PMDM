package com.spachecor.ejemplos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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

        TextView helloWorld = findViewById(R.id.HelloWorld);
        helloWorld.setText("Holi mundi");

        Button botonActividad1 = findViewById(R.id.buttonNavegacion1);
        botonActividad1.setOnClickListener(v ->{
                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    startActivity(intent);
                }
        );
        Button salirButton = findViewById(R.id.buttonAtras);
        salirButton.setOnClickListener(v->{
            finish();
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

    /*
    * onCreate: Es el primer método que se ejecuta al iniciar la actividad. Aquí se inicializa la UI y los componentes necesarios.
onStart: Llamado justo después de onCreate, indica que la actividad está a punto de ser visible para el usuario.
onResume: Aquí la actividad está en primer plano y lista para interactuar con el usuario.
onPause: Llamado cuando la actividad está parcialmente oculta, pero aún visible (por ejemplo, aparece un cuadro de diálogo o se lanza otra actividad encima).
onStop: La actividad ya no es visible, generalmente porque el usuario la ha minimizado o navegado a otra.
onDestroy: Llamado justo antes de que la actividad sea completamente destruida, por ejemplo, cuando el usuario cierra la aplicación o el sistema necesita liberar recursos.
    * */
}
