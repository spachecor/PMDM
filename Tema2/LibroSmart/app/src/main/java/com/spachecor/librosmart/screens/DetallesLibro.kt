package com.spachecor.librosmart.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.spachecor.librosmart.currentFontGlobal
import com.spachecor.librosmart.listaService
import com.spachecor.librosmart.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallesLibro(navController: NavController, nombreLista: String?, isbnLibro: Long?) {
    val lista = listaService.obtenerLista(nombreLista)
    if (lista == null) navController.popBackStack()
    val libro = listaService.obtenerLibro(lista, isbnLibro)
    if (libro == null) navController.popBackStack()

    Scaffold(
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
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = nombreLista ?: "Lista desconocida",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = currentFontGlobal
                            ),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) // Más discreto
                        )
                        Text(
                            text = libro?.titulo ?: "Libro no disponible",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = currentFontGlobal,
                                fontWeight = FontWeight.Bold
                            ), // Destacado
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val lista = listaService.obtenerLista(nombreLista)
                        if (lista != null) {
                            val libro = listaService.obtenerLibro(lista, isbnLibro)
                            if (libro != null) {
                                lista.libros.remove(libro)
                                listaService.actualizarLista(lista)
                                navController.popBackStack()
                            } else {
                                navController.navigate(AppScreens.HomePage.route)                            }
                        } else {
                            navController.navigate(AppScreens.HomePage.route)                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
        ) {
            LazyColumn {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Spacer(modifier = Modifier.height(20.dp))
                            InfoRow(label = "ISBN:", value = libro?.isbn?.toString() ?: "No disponible")
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            InfoRow(label = "Título:", value = libro?.titulo ?: "No disponible")
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            InfoRow(label = "Autor:", value = libro?.autor ?: "No disponible")
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            InfoRow(label = "Nº páginas:", value = libro?.getnPaginas()?.toString() ?: "No disponible")
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            InfoRow(label = "Opinión:", value = libro?.opinion ?: "No disponible")
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }
                }
            }
        }
    }
}