package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.app.data.user.User
import com.example.app.viewModel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private var _user: MutableLiveData<User> = MutableLiveData()

    private lateinit var editTextPhone: EditText
    private lateinit var confirmButton: Button

    private var confirmed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        editTextPhone = findViewById(R.id.editTextPhone)
        confirmButton = findViewById(R.id.confirmButton)
        confirmButton.setOnClickListener {
            setUser()
        }

        // Set up the buttons
        findViewById<Button>(R.id.buttonLocations).setOnClickListener {
            // start the location activity
            val intent = Intent(this, StationActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.buttonStart).setOnClickListener {
            if (!confirmed) {
                Toast.makeText(this, "Please confirm your phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = _user.value!!
            // start the qr code activity
            if (user.bikeId.toInt() == -1) {
                val intent = Intent(this, RequestActivity::class.java)
                intent.putExtra("requestCode", 0)
                intent.putExtra("phone", user.phone)
                startActivity(intent)
            } else {
                Toast.makeText(this, "You already have a bike", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.buttonEnd).setOnClickListener {
            if (!confirmed) {
                Toast.makeText(this, "Please confirm your phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = _user.value!!
            // start the qr code activity
            if (user.bikeId.toInt() != -1) {
                val intent = Intent(this, RequestActivity::class.java)
                intent.putExtra("requestCode", 1)
                intent.putExtra("phone", user.phone)
                startActivity(intent)
            } else {
                Toast.makeText(this, "You don't have a bike", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        confirmed = false
        if (findViewById<EditText>(R.id.editTextPhone).text.length == 9) {
            setUser()
        }
    }

    private fun setUser() {
        // check if phone number is valid
        val phone = editTextPhone.text.toString()
        if (phone.length != 9) {
            editTextPhone.error = "Please enter a valid phone number"
            editTextPhone.requestFocus()
        }

        // check if phone number is in database
        userViewModel.userByPhone.observe(this) { _user ->
            if (_user == null) {
                // add user to database
                val newUser = User(phone, "user", "user")
                userViewModel.insertUser(newUser)
                this._user.postValue(newUser)
                confirmed = true
            } else {
                this._user.postValue(_user)
                confirmed = true
            }
        }
        userViewModel.getUserByPhone(phone)
    }
}