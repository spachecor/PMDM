package com.spachecor.librosmart.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spachecor.librosmart.screens.DetallesLibro
import com.spachecor.librosmart.screens.HomePage
import com.spachecor.librosmart.screens.NuevoLibro
import com.spachecor.librosmart.screens.SettingsPage
import com.spachecor.librosmart.screens.VistaLista

@Composable
fun AppNavigation(isDarkTheme: MutableState<Boolean>, isCatFont: MutableState<Boolean>, isSquareTheme: MutableState<Boolean>){
    //se encarga de propagar los parámetros entre las pantallas
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppScreens.HomePage.route
    ){
        composable(route = AppScreens.HomePage.route){
            HomePage(navController)
        }
        composable(
            route = AppScreens.VistaLista.route+"/{nombreLista}",
            arguments = listOf(navArgument(name = "nombreLista"){
                type = NavType.StringType
            })
        ){
            VistaLista(navController, it.arguments?.getString("nombreLista"))
        }
        composable(
            route = AppScreens.DetallesLibro.route + "/{nombreLista}/{isbnLibro}",
            arguments = listOf(
                navArgument(name = "nombreLista") { type = NavType.StringType },
                navArgument(name = "isbnLibro") { type = NavType.LongType }
            )
        ) {
            DetallesLibro(
                navController,
                it.arguments?.getString("nombreLista"),
                it.arguments?.getLong("isbnLibro")
            )
        }
        composable(
            route = AppScreens.NuevoLibro.route + "/{nombreLista}",
            arguments = listOf(navArgument(name = "nombreLista"){
                type = NavType.StringType
            })
        ){
            NuevoLibro(navController, it.arguments?.getString("nombreLista"))
        }
        composable(route = AppScreens.SettingsPage.route){
            SettingsPage(
                navController,
                isDarkTheme = isDarkTheme.value,
                isCatFont = isCatFont.value,
                isSquareTheme = isSquareTheme.value,
                onThemeChange = { isDarkTheme.value = it },
                onFontChange = {
                    isCatFont.value = it
                    if(isCatFont.value)isSquareTheme.value=false
                    },
                onSquareThemeChange = {
                    isSquareTheme.value = it
                    if(isSquareTheme.value)isCatFont.value=false
                }
            )
        }
    }
}