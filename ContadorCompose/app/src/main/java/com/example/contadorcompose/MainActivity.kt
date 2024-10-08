package com.example.contadorcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            MessageCard(Message("Android", "Jetpack Compose"))
        }
    }
}

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message) {
    Row (modifier = Modifier.padding(all = 50.dp)){
        Image(
            painter = painterResource(R.drawable.f94673f2c4af0aced9a84ee5b4b038ce),
            contentDescription = "Contact profile picture",
        )

        Column {
            Text(text = msg.author)
            Text(text = msg.body)
        }

    }
}
/*
@Composable
fun isTerrible(){
    Text("Es terrible!!!")
}

@Composable
fun isNotTerrible(){
    Text("No es terrible!!!")
}*/

@Preview(showBackground = true)
@Composable
fun PreviewMessageCard() {
        MessageCard(
            msg = Message("Lexi", "Hey, take a look at Jetpack Compose, it's great!")
        )
}