package com.spachecor.librosmart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.spachecor.librosmart.screens.*

@Composable
fun AppNavigation(){
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
        /*TODO IR AGREGANDO LAS NUEVAS VISTAS*/
    }
}