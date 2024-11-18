package com.spachecor.librosmart.model.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spachecor.librosmart.model.entity.Libro;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonService {
    public static Libro obtenerLibroPorISBN(Long isbn){
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:"+isbn.toString()+"&format=json&jscmd=data";
        try {

            URL myurl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) myurl.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestMethod("GET");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
            return leerLoNecesario(content.toString());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Método que toma un json en formato string y toma de él los datos necesarios para crear un libro
     * @param jsonString El json en formato string
     * @return El libro creado a partir del json. Es null si no hay libro
     */
    private static Libro leerLoNecesario(String jsonString){
        //parsear el JSON a un JsonObject
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

        //iterar sobre todas las claves
        for (String isbn : jsonObject.keySet()) {
            JsonObject book = jsonObject.getAsJsonObject(isbn);

            //extraer datos del libro
            String title = book.get("title").getAsString();
            String author = book.getAsJsonArray("authors").get(0).getAsJsonObject().get("name").getAsString();
            int numberOfPages = book.get("number_of_pages").getAsInt();

            //Crear el libro y devolverlo
            return new Libro(Long.parseLong(isbn.replace("ISBN:", "")), title, author, numberOfPages, "");
        }
        return null;
    }
}
