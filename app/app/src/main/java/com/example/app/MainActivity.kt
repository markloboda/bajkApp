package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Set up the buttons
        findViewById<Button>(R.id.buttonLocations).setOnClickListener {
            // start the location activity
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
        }

    }
}