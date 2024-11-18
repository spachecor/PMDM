package com.spachecor.librosmart.model.service;

import java.util.ArrayList;
import java.util.List;

public class ValidadorISBN {
    public static boolean validarISBN(String isbn) {
        boolean validado = false;
        List<Integer> listaNumeros = new ArrayList<>();
        int indiceX = isbn.indexOf('X');

        if (isbn != null && !isbn.trim().isEmpty()) {
            try {
                if (isbn.length() == 10) {
                    if (indiceX == 9 || indiceX == -1) {
                        for (int i = 0; i <= 8; i++) {
                            listaNumeros.add(Integer.parseInt(Character.toString(isbn.charAt(i))) * (i + 1));
                        }

                        int modulo = listaNumeros.stream().mapToInt(Integer::intValue).sum() % 11;
                        int digitoControl = (isbn.charAt(9) == 'X') ? 10 : Integer.parseInt(Character.toString(isbn.charAt(9)));

                        validado = (digitoControl == modulo);
                    }
                } else if (isbn.length() == 13) {
                    if (indiceX == -1) {
                        for (int i = 0; i <= 11; i++) {
                            listaNumeros.add((i % 2 == 0) ?
                                    Integer.parseInt(Character.toString(isbn.charAt(i))) :
                                    Integer.parseInt(Character.toString(isbn.charAt(i))) * 3);
                        }

                        int modulo = listaNumeros.stream().mapToInt(Integer::intValue).sum() % 10;
                        modulo = (modulo == 0) ? 0 : 10 - modulo;

                        int digitoControl = Integer.parseInt(Character.toString(isbn.charAt(12)));
                        validado = (digitoControl == modulo);
                    }
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return validado;
    }

}
