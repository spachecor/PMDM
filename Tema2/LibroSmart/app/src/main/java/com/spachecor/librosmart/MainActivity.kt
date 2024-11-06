package com.spachecor.librosmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spachecor.librosmart.model.entity.Libro
import com.spachecor.librosmart.model.entity.Lista
import com.spachecor.librosmart.model.service.ListaService
import com.spachecor.librosmart.ui.theme.LibroSmartTheme
//declaramos listaService como variable global
lateinit var listaService: ListaService
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        listaService = ListaService(this)
        setContent {
            LibroSmartTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomePage(innerPadding)
                }
            }
        }
    }
}

@Composable
fun HomePage(innerPadding: PaddingValues){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(innerPadding)
    ){
        var listas = listaService.obtenerTodasListas()
        //barra de búsqueda de libros
        BarraNavegacionListas(listas)
        //en esta fila está el título de lista y el botón para crear una nueva
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            //titulo
            Text(
                text = "Listas",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
            //boton de nueva lista
            Button(
                onClick = {
                    println("Me pulsaste!!")
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar nueva lista",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
            }
        }
        //tarjetas
        for ((index, item) in listas.withIndex()) {
            if (index >= listas.size - 1) {
                ListasCard(item.nombre)  // Pasa el valor de item convertido a String
            }else{

            }
        }
        ListasCard("Por leer", "Leídos")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibroSmartTheme {

    }
}