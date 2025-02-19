package com.spachecor.roomexample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txtUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.txtUsuarios = findViewById(R.id.txtUsuarios);

        AppDatabase appDatabase = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class,
                "dbprueba"
        ).allowMainThreadQueries().build();

        //appDatabase.daoUsuario().insertarUsuario(new Usuario("spachecor", "Selene", "spachecor03@gmail.com"));
        //appDatabase.daoUsuario().insertarUsuario(new Usuario("juando", "Juan Domingo", "juando90@gmail.com"));
        //appDatabase.daoUsuario().insertarUsuario(new Usuario("peque", "La peque", "laPeque@gmail.com"));

        List<Usuario> usuarios = appDatabase.daoUsuario().obtenerUsuarios();
        String texto = "";
        for(Usuario usuario:usuarios){
            texto+="Usuario: "+usuario.usuario+", Correo: "+usuario.correo+", Nombre: "+usuario.nombre;
        }
        txtUsuarios.setText(texto);
    }
}