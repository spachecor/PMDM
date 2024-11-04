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
import com.spachecor.librosmart.model.entity.Lista
import com.spachecor.librosmart.ui.theme.LibroSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
        val listas = listOf(
            Lista("Lista 1"),
            Lista("Lista 2"),
            Lista("Lista 3")
        )
        BarraNavegacion(listas)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibroSmartTheme {

    }
}