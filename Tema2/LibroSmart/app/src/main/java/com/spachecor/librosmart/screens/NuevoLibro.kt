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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.spachecor.librosmart.model.service.JsonService
import com.spachecor.librosmart.model.service.ValidadorISBN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoLibro(navController: NavController, nombreLista: String?) {
    var listaElegida by remember { mutableStateOf("") }
    if (nombreLista.isNullOrEmpty()) listaElegida = "Seleccione una lista"
    else listaElegida = nombreLista
    var isbn by remember { mutableStateOf("") }
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var nPaginas by remember { mutableStateOf("") }
    var opinion by remember { mutableStateOf("") }
    var expandido by remember { mutableStateOf(false) }
    val listas = listaService.obtenerTodasListas()

    // Variable para manejar la operación de red
    var loading by remember { mutableStateOf(false) }

    // Definir un Scope para coroutines
    val coroutineScope = rememberCoroutineScope()

    var errorMessage by remember { mutableStateOf("") }

    fun validateForm(): Boolean {
        // Validaciones
        if (listaElegida == "Seleccione una lista") {
            errorMessage = "Debe seleccionar una lista."
            return false
        }
        if (isbn.length !in listOf(10, 13)) {
            errorMessage = "El ISBN debe tener 10 o 13 caracteres."
            return false
        }
        if (isbn.toLongOrNull() == null) {
            errorMessage = "El ISBN debe ser numérico."
            return false
        }
        if(!ValidadorISBN.validarISBN(isbn)){
            errorMessage = "El ISBN no es válido."
            return false
        }
        if (titulo.isEmpty()) {
            errorMessage = "El título no puede estar vacío."
            return false
        }
        if (autor.isEmpty()) {
            errorMessage = "El autor no puede estar vacío."
            return false
        }
        if (nPaginas.isEmpty() || nPaginas.toIntOrNull() == null || nPaginas.toInt() <= 0 || nPaginas.toInt() > 10000) {
            errorMessage = "El número de páginas debe ser un número entre 1 y 10,000."
            return false
        }
        if (opinion.isEmpty()) {
            errorMessage = "La opinión no puede estar vacía."
            return false
        }
        return true
    }

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
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Nuevo libro",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = currentFontGlobal,
                                fontWeight = FontWeight.Bold
                            ),
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
                                            .padding(end = 16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        TextField(
                                            value = isbn,
                                            onValueChange = { isbn = it },
                                            placeholder = { Text("Ej: 8423309940") },
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = {
                                            /* todo SIGUE SIN FUNCIONAR*/
                                                if (isbn.toLongOrNull() == null) {
                                                    errorMessage = "El ISBN debe tener 10 o 13 caracteres."
                                                }
                                                else if (isbn.length !in listOf(10, 13)) {
                                                    errorMessage = "El ISBN debe ser numérico."
                                                }
                                                else if(!ValidadorISBN.validarISBN(isbn)){
                                                    errorMessage = "El ISBN no es válido."
                                                }else{
                                                    coroutineScope.launch {
                                                        // Ejecutar la llamada de red en un hilo en segundo plano
                                                        val libro = withContext(Dispatchers.IO) {
                                                            JsonService.obtenerLibroPorISBN(isbn.toLongOrNull())
                                                        }
                                                        if (libro != null) {
                                                            // Si se obtiene un libro, actualizar los campos
                                                            titulo = libro.titulo
                                                            autor = libro.autor
                                                            nPaginas = libro.getnPaginas().toString()
                                                            errorMessage = ""
                                                        } else {
                                                            errorMessage = "No se encontró el libro."
                                                        }
                                                    }
                                                }
                                            },
                                            modifier = Modifier.size(40.dp)
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
                            text = errorMessage,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }
                    Button(
                        onClick = {
                            if (validateForm()) {
                                //buscamos la lista en las listas
                                val lista = listas.find { it.nombre.equals(listaElegida) }
                                if (lista != null) lista.libros.add(
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
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = "Guardar",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = currentFontGlobal
                        )
                    }
                }
            }
        }
    }
}
