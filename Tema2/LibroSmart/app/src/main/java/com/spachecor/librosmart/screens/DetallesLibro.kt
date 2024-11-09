package com.spachecor.librosmart.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.spachecor.librosmart.listaService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesLibro(navController: NavController, nombreLista: String?, isbnLibro: Long?){
    val lista = listaService.obtenerLista(nombreLista)
    if (lista==null)navController.popBackStack()
    var libro = listaService.obtenerLibro(lista, isbnLibro)
    if (libro==null)navController.popBackStack()
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retroceso"
                        )
                    }
                },
                title = { Text(text = nombreLista+" -> "+libro.titulo) }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
        ) {
            LazyColumn {
                item {
                    Text(text = "ISBN: ${libro?.isbn?: "No disponible"}")
                    Text(text = "Título: ${libro?.titulo?: "No disponible"}")
                    Text(text = "Autor: ${libro?.autor?: "No disponible"}")
                    Text(text = "Nº páginas: ${libro?.getnPaginas()?: "No disponible"}")
                    Text(text = "Opinión: ${libro?.opinion?: "No disponible"}")
                }
            }
        }
    }
}