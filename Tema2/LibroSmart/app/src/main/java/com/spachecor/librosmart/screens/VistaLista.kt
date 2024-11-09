package com.spachecor.librosmart.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.spachecor.librosmart.listaService
import com.spachecor.librosmart.model.entity.TipoEntidad
import com.spachecor.librosmart.navigation.AppScreens

@Composable
fun VistaLista(navController: NavController, nombreLista: String?){
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) }
    ){innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(innerPadding)
        ){
            if(nombreLista==null)navController.navigate(AppScreens.HomePage.route)
            val librosLista = listaService.obtenerLista(nombreLista)
            if(librosLista == null)navController.navigate(AppScreens.HomePage.route)
            BarraNavegacion(librosLista.libros, TipoEntidad.LIBRO, navController)
            if (nombreLista != null)Titulo(nombreLista)
            LazyColumn {
                items(librosLista.libros){libro->
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .height(180.dp),
                        onClick = {
                            navController.navigate(AppScreens.DetallesLibro.route+"/"+nombreLista+"/"+libro.isbn)
                        }
                    ) {
                        Box(
                            Modifier.fillMaxSize()
                        ) {
                            Text(text = libro.titulo,
                                fontSize = 24.sp,
                                modifier = Modifier.align(Alignment.Center).padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}