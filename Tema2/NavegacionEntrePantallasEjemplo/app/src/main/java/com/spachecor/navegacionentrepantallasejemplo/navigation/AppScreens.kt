package com.spachecor.navegacionentrepantallasejemplo.navigation

sealed class AppScreens(val route: String) {
    object FirstScreen: AppScreens("first-screen")
    object SecondScreen: AppScreens("second-screen")
}