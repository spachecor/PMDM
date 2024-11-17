package com.spachecor.librosmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import com.spachecor.librosmart.model.service.ListaService
import com.spachecor.librosmart.navigation.AppNavigation
import com.spachecor.librosmart.ui.theme.AustieBostKittenKlub
import com.spachecor.librosmart.ui.theme.LibroSmartTheme
import com.spachecor.librosmart.ui.theme.Square
import com.spachecor.librosmart.ui.theme.TimesNewRoman

//declaramos listaService como variable global
lateinit var listaService: ListaService
lateinit var currentFontGlobal: FontFamily
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        listaService = ListaService(this)
        setContent {
            val isDarkTheme = remember { mutableStateOf(false) }
            val isCatFont = remember { mutableStateOf(false) }
            val isSquareTheme = remember { mutableStateOf(false) }
            val currentFont = if (isCatFont.value) {
                AustieBostKittenKlub
            } else if (isSquareTheme.value){
                Square
            }else TimesNewRoman
            currentFontGlobal = currentFont
            LibroSmartTheme (
                darkTheme = isDarkTheme.value,
                typography = currentFont,
                isSquareTheme = isSquareTheme.value
            ){
                AppNavigation(isDarkTheme = isDarkTheme, isCatFont = isCatFont, isSquareTheme = isSquareTheme)
            }
        }
    }
}