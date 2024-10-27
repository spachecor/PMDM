package com.spachecor.librosmart

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.spachecor.librosmart.ui.theme.LibroSmartTheme

@OptIn(ExperimentalMaterial3Api::class)//se pone porque searchbar es experimental
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LibroSmartTheme {
                val ctx = LocalContext.current
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("Home") })
                    },
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) { innerPadding ->
                    /*
                    * query: Almacena el texto de búsqueda que el usuario escribe en el SearchBar.
                    * remember { mutableStateOf("") }: Define query como un valor mutable que
                    * recuerda su estado actual en la composición de Jetpack Compose. Así, cada vez
                    * que query cambia (por ejemplo, cuando el usuario escribe), el SearchBar se
                    * actualiza sin recrear la composición completa.
                    * "": Inicializa query como un string vacío, es decir, sin ningún texto en el
                    * campo de búsqueda.
                    * */
                    var query by remember { mutableStateOf("") }
                    /*
                    * active: Controla si la barra de búsqueda está activa o inactiva.
                    * remember { mutableStateOf(false) }: Establece active como un valor mutable
                    * que recuerda su estado. Cuando active cambia (al abrir o cerrar el SearchBar),
                    * Compose actualiza la interfaz para reflejar el cambio sin afectar otros
                    * elementos.
                    * false: Inicializa active como false, lo que indica que la barra de búsqueda
                    * comienza inactiva hasta que el usuario interactúe con ella.
                    * */
                    var active by remember { mutableStateOf(false) }
                    /*
                    * En conjunto, ambas variables se actualizan de forma reactiva y permiten que
                    * el SearchBar responda a cambios en el texto y el estado (activo o inactivo)
                    * según la interacción del usuario.
                    * */
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        SearchBar(
                            query = query,//el valor que se muestra en la búsqueda en ese momento
                            //usar aqui el it es equivalente a: onQueryChange = { newText -> query = newText }
                            onQueryChange = {query = it},//cuando el usuario escribe
                            onSearch = {
                                //El segundo parametro es lo que nos muestra a la hora de buscar como lo
                                //que hemos buscado
                                //aquí tenemos el código que se ejecuta cuando busquemos
                                Toast.makeText(ctx, query, Toast.LENGTH_SHORT).show()
                                active = false//desactiva la barra cuando le damos a buscar o a intro
                            },//cuando se lanza la accion de busqueda
                            active = active,//indica si la barra de búsqueda está activa o no
                            onActiveChange = {active = it},//nos avisa de cuando el valor esté cambiando
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            //cuando escribamos algo muestra, si no se queda en blanco
                            if(query.isNotEmpty()){
                                //it.second.contains(query, true) toma del segundo parametro de it lo
                                // que coincida con lo que el usuario mete ignorando mayúsuculas o
                                // minúsculas, por lo que realmente si le metemos el emoji no va a buscar,
                                //solo busca sobre la segunda columnas, los nombres
                                val filteredAnimals = animals.filter { it.second.contains(query, true) }
                                LazyColumn {
                                    items(filteredAnimals){(emoji, espanol) ->
                                        Text(
                                            text = "$emoji $espanol",
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .clickable {
                                                    Toast.makeText(ctx, espanol, Toast.LENGTH_SHORT).show()
                                                    active = false
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
    }
}

val animals = listOf(
    "\uD83D\uDC07" to "conejo",
    "\uD83D\uDC08" to "gato",
    "\uD83D\uDC15" to "perro",
    "\uD83D\uDC0D" to "serpiente",
    "\uD83E\uDD81" to "león",
    "\uD83D\uDC05" to "tigre",
    "\uD83D\uDC18" to "elefante",
    "\uD83D\uDC12" to "mono",
    "\uD83E\uDD92" to "girafa",
    "\uD83E\uDD93" to "cebra"
    )

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibroSmartTheme {
    }
}