package com.example.segundaapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        //VINCULO CON LA VISTA
        setContentView(R.layout.activity_main)
        //AQUI MODIFICAMOS LA VISTA
        val textView = findViewById<TextView>(R.id.textView)
        val buttonCambioTexto = findViewById<Button>(R.id.buttonCambioTexto)
        val buttonReset = findViewById<Button>(R.id.buttonReset)
        val textoOriginal = textView.text
        //ACCION - CAMBIAR TEXTO
        buttonCambioTexto.setOnClickListener{
            textView.setText("Texto cambiado")
        }
        //ACCION - CAMBIAR COLOR TEXTO Y REESTABLECER TEXTO
        buttonReset.setOnClickListener {
            textView.setText(textoOriginal)
            textView.setTextColor(randomColor())
        }
    }
    private fun randomColor(): Int {
        val colores = listOf(
            android.R.color.holo_red_light,
            android.R.color.holo_blue_light,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_purple
        )
        val randomIndex = Random.nextInt(colores.size)
        return ContextCompat.getColor(this, colores[randomIndex])
    }
}