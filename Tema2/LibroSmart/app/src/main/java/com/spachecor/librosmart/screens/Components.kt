package com.spachecor.librosmart.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.spachecor.librosmart.currentFontGlobal
import com.spachecor.librosmart.listaService
import com.spachecor.librosmart.model.entity.Libro
import com.spachecor.librosmart.model.entity.Lista
import com.spachecor.librosmart.model.entity.TipoEntidad
import com.spachecor.librosmart.navigation.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacion(elementos: List<Any>, tipoEntidad: TipoEntidad, nombreLista: String, navController: NavController) {
    val ctx = LocalContext.current
    var query by rememberSaveable { mutableStateOf("") }
    var active by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
                if (query.isNotEmpty()) {
                    Toast.makeText(ctx, "Selecciona una opción válida en la lista", Toast.LENGTH_SHORT).show()
                    active = false
                    query = ""
                }
            },
            active = active,
            onActiveChange = { active = it },
            placeholder = { Text("Escriba aquí") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            trailingIcon = {
                IconButton(
                    onClick = {
                        Toast.makeText(ctx, "Selecciona una opción válida en la lista", Toast.LENGTH_SHORT).show()
                        active = false
                        query = ""
                    },
                    enabled = query.isNotEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            }
        ) {
            if (query.isNotEmpty()) {
                //filtrar según el tipo de entidad
                val filteredItems = when (tipoEntidad) {
                    TipoEntidad.LIBRO -> {
                        elementos.filterIsInstance<Libro>().filter { it.titulo.contains(query, ignoreCase = true) }
                    }
                    TipoEntidad.LISTA -> {
                        elementos.filterIsInstance<Lista>().filter { it.nombre.contains(query, ignoreCase = true) }
                    }
                }

                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp)
                ) {
                    items(filteredItems) { item ->
                        when (item) {
                            is Libro -> {
                                Text(
                                    text = item.titulo,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            navController.navigate(AppScreens.DetallesLibro.route+"/"+nombreLista+"/"+item.isbn)
                                            active = false
                                            query = ""
                                        }
                                )
                            }
                            is Lista -> {
                                Text(
                                    text = item.nombre,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            navController.navigate(route = AppScreens.VistaLista.route+"/"+item.nombre)
                                            active = false
                                            query = ""
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
            label = { Text(
                text = "Inicio",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = currentFontGlobal
                )
            ) },
            selected = false,
            onClick = { navController.navigate(AppScreens.HomePage.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Add, contentDescription = "Nuevo libro") },
            label = { Text(
                text = "Nuevo libro",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = currentFontGlobal
                )
            ) },
            selected = false,
            onClick = { navController.navigate(AppScreens.NuevoLibro.route+"/") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
            label = { Text(
                text = "Ajustes",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = currentFontGlobal
                )
            ) },
            selected = false,
            onClick = { navController.navigate(AppScreens.SettingsPage.route) }
        )
    }
}

/**
 * Método que define el componente de la fila en el home con las listas
 */
@Composable
fun Titulo(
    showDialog: MutableState<Boolean>,
    inputValue: MutableState<TextFieldValue>,
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Listas",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        Button(
            onClick = { showDialog.value = true },
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Agregar nueva lista",
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
        if (showDialog.value) {
            var errorMessage by rememberSaveable { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = {
                    showDialog.value = false
                    errorMessage = ""
                },
                title = { Text("Introduce el nombre de la nueva lista") },
                text = {
                    Column {
                        // Mensaje de error
                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        // Campo de texto para el nombre de la lista
                        OutlinedTextField(
                            value = inputValue.value,
                            onValueChange = { inputValue.value = it },
                            label = { Text("Nombre") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val name = inputValue.value.text
                            when {
                                name.isEmpty() -> errorMessage = "El nombre no puede estar vacío."
                                name.length < 3 -> errorMessage = "El nombre debe tener al menos 3 caracteres."
                                name.length > 10 -> errorMessage = "El nombre no puede tener más de 10 caracteres."
                                else -> {
                                    // Si pasa las validaciones, agrega la lista
                                    listaService.agregarLista(Lista(name))
                                    showDialog.value = false
                                    inputValue.value = TextFieldValue("")
                                    errorMessage = ""
                                    navController.navigate(AppScreens.HomePage.route)
                                }
                            }
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog.value = false
                        inputValue.value = TextFieldValue("")
                        errorMessage = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }

    }
}


/**
 * Método que define el componente de la fila en al pantalla de la fila con los libros
 */
@Composable
fun Titulo(navController: NavController, nombre: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        )
        Button(
            onClick = { navController.navigate(AppScreens.NuevoLibro.route + "/" + nombre) },
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Agregar nuevo libro",
                modifier = Modifier.size(16.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
fun ListasCard(nombreUno: String, nombreDos: String, navController: NavController){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(
            modifier = Modifier
                .size(width = 180.dp, height = 115.dp)
                .padding(16.dp),
            onClick = {
                navController.navigate(route = AppScreens.VistaLista.route+"/"+nombreUno)
            }
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                Text(
                    text = nombreUno,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        Card(
            modifier = Modifier
                .size(width = 180.dp, height = 115.dp)
                .padding(16.dp),
            onClick = {
                navController.navigate(route = AppScreens.VistaLista.route+"/"+nombreDos)
            }
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                Text(
                    text = nombreDos,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
@Composable
fun ListasCard(nombreUno: String, navController: NavController){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(
            modifier = Modifier
                .size(width = 180.dp, height = 115.dp)
                .padding(16.dp),
            onClick = {
                navController.navigate(route = AppScreens.VistaLista.route+"/"+nombreUno)
            }
        ) {
            Box(
                Modifier.fillMaxSize()
            ) {
                Text(
                    text = nombreUno,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 22.sp,  // Aumento de tamaño de letra
            fontWeight = FontWeight.Bold,  // Negrita en la parte izquierda
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )
        Text(
            text = value,
            fontSize = 20.sp,  // Aumento de tamaño de letra
            modifier = Modifier.weight(1f)
        )
    }
}
@Composable
fun FormularioRow(label: String, value: @Composable () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        )
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
        ) {
            value()
        }
    }
}
@Composable
fun ToggleSwitch(isChecked: Boolean, onToggle: (Boolean) -> Unit, modifier: Modifier){
    Switch(
        checked = isChecked,
        onCheckedChange = { onToggle(it)},
        modifier = modifier
    )
}