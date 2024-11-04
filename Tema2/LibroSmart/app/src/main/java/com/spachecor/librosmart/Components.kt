package com.spachecor.librosmart

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.spachecor.librosmart.model.entity.Lista

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraNavegacion(listas : List<Lista>) {
    val ctx = LocalContext.current
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    SearchBar(
        query = query,
        onQueryChange = {query = it},
        onSearch = {
            if(query.isNotEmpty()){
                Toast.makeText(ctx, query, Toast.LENGTH_SHORT).show()
                active = false
            }
        },
        active = active,
        onActiveChange = {active = it},
        placeholder = { Text("Escriba aquí") },
        trailingIcon = {
            IconButton(
                onClick = {
                    //MISMO CONTENIDO QUE EL ONSEARCH
                    Toast.makeText(ctx, query, Toast.LENGTH_SHORT).show()
                    active = false
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
        if(query.isNotEmpty()){
            val filteredListas = listas.filter { it.nombre.contains(query, true) }
            LazyColumn {
                items(filteredListas){lista ->
                    Text(
                        text = lista.nombre,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                Toast.makeText(ctx, lista.nombre, Toast.LENGTH_SHORT).show()
                                active = false
                            }
                    )
                }
            }
        }
    }
}