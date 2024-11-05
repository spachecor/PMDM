package com.spachecor.librosmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContentProviderCompat.requireContext
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
        Modifier.fillMaxSize().padding(innerPadding)
    ){
        val porLeer = Lista("Por leer").apply {
            libros.add(Libro(9788466679923L, "TODO MUERE", "JUAN GOMEZ JURADO", 234, "Todo ok"))
            libros.add(Libro(9788466679923L, "CORONA DE MEDIANOCHE", "SARAH J. MAAS", 234, "Todo ok"))
        }
        listaService.agregarLista(porLeer)
        BarraNavegacionListas(listaService.obtenerTodasListas())
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibroSmartTheme {

    }
}