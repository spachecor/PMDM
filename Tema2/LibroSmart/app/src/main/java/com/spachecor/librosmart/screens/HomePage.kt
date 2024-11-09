package com.spachecor.librosmart.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.spachecor.librosmart.listaService
import com.spachecor.librosmart.model.entity.Libro
import com.spachecor.librosmart.model.entity.Lista
import com.spachecor.librosmart.model.entity.TipoEntidad

@Composable
fun HomePage(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
        ) {
            val librosPorLeer = listOf(
                Libro(9781234567897, "El principito", "Antoine de Saint-Exupéry", 96, "Un clásico inolvidable."),
                Libro(9789876543210, "1984", "George Orwell", 328, "Una visión aterradora del futuro."),
                Libro(9788525043206, "Cien años de soledad", "Gabriel García Márquez", 417, "Una obra maestra del realismo mágico."),
                Libro(9780140449136, "Crimen y castigo", "Fyodor Dostoevsky", 671, "Profundo y provocador.")
            )
            listaService.agregarLista(Lista("Por leer", librosPorLeer))
            val listas = listaService.obtenerTodasListas()

            //barra de búsqueda de listas
            BarraNavegacion(listas, TipoEntidad.LISTA, navController)

            //título de listas y botón de nueva lista
            Titulo()

            //agrupar las listas en pares para evitar desorden al hacer scroll
            val listasEnPares = listas.chunked(2)

            LazyColumn {
                items(listasEnPares) { par ->
                    if (par.size == 2) {
                        //si el par tiene dos elementos, los muestra juntos
                        ListasCard(par[0].nombre, par[1].nombre, navController)
                    } else {
                        // Si el par tiene solo un elemento, lo muestra solo
                        ListasCard(par[0].nombre, navController)
                    }
                }
            }
        }
    }
}