package com.spachecor.librosmart.model.service;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spachecor.librosmart.model.entity.Libro;
import com.spachecor.librosmart.model.entity.Lista;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListaService {
    private final File file;
    private final Gson gson;
    private List<Lista> listas;

    public ListaService(Context context) {
        file = new File(context.getFilesDir(), "listas.json");
        gson = new GsonBuilder().setPrettyPrinting().create();

        // Si el archivo no existe, créalo vacío
        if (!file.exists()) {
            listas = new ArrayList<>();
            guardarListas();
        } else {
            listas = obtenerListas();
        }
    }

    /**
     * Método que obtiene una lista de listas de libros
     * @return Una lista de listas de libros
     */
    private List<Lista> obtenerListas(){
        try(Reader reader = new FileReader(file)){
            Type listType = new TypeToken<List<Lista>>(){}.getType();
            List<Lista> listasTemp =gson.fromJson(reader, listType);
            if(listasTemp==null){
                return new ArrayList<>();
            }else return listasTemp;
        }catch (IOException e){
            return new ArrayList<>();
        }
    }

    /**
     * Método que guarda una lista de listas de libros en el archivo json
     * @return true o false según si se ha conseguido guardar la lista
     */
    private boolean guardarListas(){
        try(Writer writer = new FileWriter(file)){
            gson.toJson(listas, writer);
            return Boolean.TRUE;
        }catch (IOException e){
            return Boolean.FALSE;
        }
    }

    /**
     * Método que agrega una nueva lista a las listas contenidas ne el json
     *
     * @return true o false según si se realiza correctamente o no
     */
    public boolean agregarLista(Lista lista){
        if(!listas.contains(lista)){
            listas.add(lista);
            guardarListas();
            return Boolean.TRUE;
        }else return Boolean.FALSE;
    }

    /**
     * Método que elimina una lista de las listas contenidas en el json
     * @param nombre El nombre de la lista a eliminar
     * @return true o false según si se elimina o no
     */
    public boolean eliminarLista(String nombre){
        //el método removeIf, que toma un argumento que es un predicado y define la condición de
        //eliminación(revisa toda la lista con esa condición)
        boolean eliminado = listas.removeIf(l -> Objects.equals(l.getNombre(), nombre));
        if(eliminado){
            return guardarListas();
        }else return Boolean.FALSE;
    }
    /**
     * Método que obtiene todas las listas del json
     * @return Una lista con las listas
     */
    public List<Lista> obtenerTodasListas(){
        return new ArrayList<>(listas);
    }
    public Lista obtenerLista(String nombreLista){
        //actualizamos las listas
        listas = obtenerListas();
        for(Lista lista: listas){
            if(lista.getNombre().equals(nombreLista))return lista;
        }
        return null;
    }
    public Libro obtenerLibro(Lista lista, Long isbn){
        Libro libro = null;
        for(Libro libroTemp: lista.getLibros()){
            if(libroTemp.getIsbn().equals(isbn))libro=libroTemp;
        }
        return libro;
    }
}
