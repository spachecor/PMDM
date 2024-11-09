package com.spachecor.librosmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.spachecor.librosmart.model.service.ListaService
import com.spachecor.librosmart.navigation.AppNavigation
import com.spachecor.librosmart.screens.*
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
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibroSmartTheme {
    }
}