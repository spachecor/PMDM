package com.spachecor.librosmart.navigation

sealed class AppScreens(val route: String) {
    object HomePage: AppScreens("home-page")
    object VistaLista: AppScreens("vista-lista")
    object NuevoLibro: AppScreens("nuevo-libro")
    object DetallesLibro: AppScreens("detalles-libro")
    object SettingsPage: AppScreens("settings-page")
}