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
            val intent = Intent(this, StationActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            // start the qr code activity
            val intent = Intent(this, RequestActivity::class.java)
            intent.putExtra("requestCode", 0)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonEnd).setOnClickListener {
            // start the qr code activity
            val intent = Intent(this, RequestActivity::class.java)
            intent.putExtra("requestCode", 1)
            startActivity(intent)
        }
    }
}