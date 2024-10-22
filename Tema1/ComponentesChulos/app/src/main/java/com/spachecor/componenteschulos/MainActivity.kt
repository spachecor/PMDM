package com.spachecor.componenteschulos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlexColumnExample()
        }
    }
}
@Composable
fun body() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Hola terrible")
    }
}
@Composable
fun FlexRowExample() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween // Espacio entre elementos
    ) {
        Box(modifier = Modifier.weight(1f).height(50.dp).background(Color.Red))
        Box(modifier = Modifier.weight(1f).height(50.dp).background(Color.Green))
        Box(modifier = Modifier.weight(1f).height(50.dp).background(Color.Blue))
    }
}

@Composable
fun FlexColumnExample() {
    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween, // Espacio entre elementos
        horizontalAlignment = Alignment.CenterHorizontally // Alineación horizontal
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Yellow))
        Box(modifier = Modifier.weight(3f).fillMaxWidth().background(Color.Magenta))
        Box(modifier = Modifier.weight(1f).fillMaxWidth().background(Color.Cyan))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlexRowExample()
}