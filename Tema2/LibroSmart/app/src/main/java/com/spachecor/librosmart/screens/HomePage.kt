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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.spachecor.librosmart.listaService
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
            val showDialog = remember { mutableStateOf(false) }
            val inputValue = remember { mutableStateOf(TextFieldValue("")) }
            val listas = listaService.obtenerTodasListas()

            //barra de búsqueda de listas
            BarraNavegacion(listas, TipoEntidad.LISTA, "", navController)

            //título de listas y botón de nueva lista
            Titulo(showDialog = showDialog, inputValue = inputValue, navController)

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