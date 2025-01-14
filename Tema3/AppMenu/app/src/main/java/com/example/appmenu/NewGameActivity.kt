package com.example.appmenu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class NewGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)  // Asegúrate de que este XML esté bien definido
    }

    fun volverAlMenu(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }
}