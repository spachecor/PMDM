package com.spachecor.librosmart.model.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.spachecor.librosmart.model.entity.Lista;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListaService {
    private static final String FILE_PATH = "src/resources/com/spachecor/librosmart/json/listas.json";
    private final Gson gson;

    public ListaService() {
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Método que obtiene una lista de listas de libros
     * @return Una lista de listas de libros
     */
    public List<Lista> obtenerListas(){
        try(Reader reader = new FileReader(FILE_PATH)){
            Type listType = new TypeToken<List<Lista>>(){}.getType();
            return gson.fromJson(reader, listType);
        }catch (IOException e){
            return new ArrayList<>();
        }
    }

    /**
     * Método que guarda una lista de listas de libros en el archivo json
     * @param listas La lista de listas de libros a guardar
     * @return true o false según si se ha conseguido guardar la lista
     */
    public boolean guardarListas(List<Lista> listas){
        try(Writer writer = new FileWriter(FILE_PATH)){
            gson.toJson(listas, writer);
            return Boolean.TRUE;
        }catch (IOException e){
            return Boolean.FALSE;
        }
    }
}
