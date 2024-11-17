package com.spachecor.librosmart.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.spachecor.librosmart.currentFontGlobal
import com.spachecor.librosmart.listaService
import com.spachecor.librosmart.model.entity.Libro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoLibro(navController: NavController, nombreLista: String?) {
    var listaElegida by remember { mutableStateOf("") }
    if(nombreLista.isNullOrEmpty())listaElegida = "Seleccione una lista"
    else listaElegida = nombreLista
    var isbn by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var nPaginas by remember { mutableStateOf("") }
    var opinion by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    val listas = listaService.obtenerTodasListas()

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
                            text = "Nuevo libro",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = currentFontGlobal,
                                fontWeight = FontWeight.Bold
                            ), // Destacado
                            color = MaterialTheme.colorScheme.onSurface
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

                            // Surface con fondo ligeramente diferente
                            FormularioRow(
                                label = "Lista:",
                                value = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { expandido = !expandido }
                                            .padding(16.dp)
                                            .background(MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.medium)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = listaElegida,
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Icon(
                                                imageVector = if (expandido) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                                contentDescription = "Desplegable",
                                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                    DropdownMenu(
                                        expanded = expandido,
                                        onDismissRequest = { expandido = false }
                                    ) {
                                        listas.forEach { lista ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    listaElegida = lista.nombre
                                                    expandido = false
                                                },
                                                text = { Text(lista.nombre) }
                                            )
                                        }
                                    }
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            FormularioRow(
                                label = "ISBN:",
                                value = {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(end = 16.dp), // Para evitar que el botón se pegue al borde
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextField(
                                            value = isbn,
                                            onValueChange = { isbn = it },
                                            placeholder = { Text("Ej: 8423309940") },
                                            modifier = Modifier.weight(1f) // Hace que el TextField ocupe todo el espacio disponible
                                        )
                                        IconButton(
                                            onClick = { /* Método vacío para agregar lógica luego */ },
                                            modifier = Modifier.size(40.dp) // Tamaño pequeño para el botón
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Search,
                                                contentDescription = "Buscar",
                                                tint = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            FormularioRow(
                                    label = "Título:",
                                value = {
                                    TextField(
                                        value = titulo,
                                        onValueChange = { titulo = it },
                                        placeholder = { Text("Ej: La Celestina") }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            FormularioRow(
                                label = "Autor:",
                                value = {
                                    TextField(
                                        value = autor,
                                        onValueChange = { autor = it },
                                        placeholder = { Text("Ej: Fernando de Rojas") }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            FormularioRow(
                                label = "Nº páginas:",
                                value = {
                                    TextField(
                                        value = nPaginas,
                                        onValueChange = { nPaginas = it },
                                        placeholder = { Text("Ej: 249") }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(20.dp))
                            FormularioRow(
                                label = "Opinión:",
                                value = {
                                    TextField(
                                        value = opinion,
                                        onValueChange = { opinion = it },
                                        placeholder = { Text("Ej: Me ha parecido...") }
                                    )
                                }
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                    Button(
                        onClick = {
                            //buscamos la lista en las listas
                            val lista = listas.find { it.nombre.equals(listaElegida) }
                            if(lista!=null)lista.libros.add(
                                Libro(
                                isbn.toLong(),
                                titulo,
                                autor,
                                nPaginas.toInt(),
                                opinion
                                )
                            )
                            listaService.actualizarLista(lista)
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Guardar",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
