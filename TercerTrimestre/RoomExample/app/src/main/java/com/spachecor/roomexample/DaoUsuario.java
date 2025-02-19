package com.spachecor.roomexample;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoUsuario {

    @Query("SELECT * FROM usuario")
    List<Usuario> obtenerUsuarios();

    @Query("SELECT * FROM usuario WHERE usuario= :usuario")
    Usuario obtenerUsuario(String usuario);

    @Insert
    void insertarUsuario(Usuario...usuario);

    @Update
    void actualizarUsuario(Usuario usuario);

    @Delete
    void borrarUsuario(Usuario usuario);
}
