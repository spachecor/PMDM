package com.spachecor.roomexample;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey
    @NonNull
    public String usuario;
    public String nombre;
    public String correo;

    public Usuario(@NonNull String usuario, String nombre, String correo) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.correo = correo;
    }
}
