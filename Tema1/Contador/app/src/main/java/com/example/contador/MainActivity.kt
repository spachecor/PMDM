package com.example.contador

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        //tomamos el contador y los botones
        val contador = findViewById<TextView>(R.id.textViewContador)
        val buttonMenos = findViewById<Button>(R.id.buttonMenos)
        val buttonMas = findViewById<Button>(R.id.buttonMas)

        buttonMenos.setOnClickListener {
            val valorContador : Int = Integer.parseInt(contador.text.toString());
            contador.setText((valorContador-1).toString())
            comprobarContadorCambiarColor(contador)
        }

        buttonMas.setOnClickListener {
            val valorContador : Int = Integer.parseInt(contador.text.toString());
            contador.setText((valorContador+1).toString())
            comprobarContadorCambiarColor(contador)
        }
    }

    fun comprobarContadorCambiarColor(contador: TextView) {
        val contadorNumero = Integer.parseInt(contador.text.toString())
        when {
            contadorNumero >= 10 -> {
                contador.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light))
            }
            contadorNumero <= -10 -> {
                contador.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light))
            }
            else -> {
                contador.setTextColor(ContextCompat.getColor(this, android.R.color.black))
            }
        }
    }
}