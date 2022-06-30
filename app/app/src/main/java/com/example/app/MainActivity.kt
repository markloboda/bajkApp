package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app.data.reservation.Reservation
import com.example.app.data.user.User
import com.example.app.recyclerView.BikesRecyclerViewAdapter
import com.example.app.viewModel.BikeViewModel
import com.example.app.viewModel.ReservationViewModel
import com.example.app.viewModel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    private lateinit var bikeViewModel: BikeViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var reservationViewModel: ReservationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.bikesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BikesRecyclerViewAdapter(this)
        recyclerView.adapter = adapter

        // ViewModel
        bikeViewModel = ViewModelProvider(this)[BikeViewModel::class.java]
        bikeViewModel.allBikes.observe(this) { bikes ->
            adapter.setBikes(bikes)
        }
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        reservationViewModel = ViewModelProvider(this)[ReservationViewModel::class.java]
    }

    fun dataDialog(bikeId: Long, bikeTitle: String, bikeStatus: Boolean) {
        if (!bikeStatus) return
        dialogBuilder = AlertDialog.Builder(this, R.style.Theme_App)
        dialogBuilder.setTitle("Izposoja kolesa $bikeTitle")
        dialogBuilder.setIcon(R.drawable.logo)
        val dataPopupView: View = layoutInflater.inflate(R.layout.reservation_dialog, null)
        dialogBuilder.setView(dataPopupView)
        dialog = dialogBuilder.create()
        dialog.show()

        // get view that contain inputed data
        val reservationIzposojevalec = dialog.findViewById<EditText>(R.id.reservationIzposojevalec)
        val reservationSektor = dialog.findViewById<EditText>(R.id.reservationSektor)
        val reservationOd = dialog.findViewById<EditText>(R.id.reservationOd)
        val reservationDo = dialog.findViewById<EditText>(R.id.reservationDo)
        val reservationKm = dialog.findViewById<SeekBar>(R.id.reservationKm)
        val reservationNamen = dialog.findViewById<EditText>(R.id.reservationNamen)

        // setup dialog buttons
        dataPopupView.findViewById<View>(R.id.reservationConfirmButton).setOnClickListener {
            if (checkInputs(
                    reservationIzposojevalec!!,
                    reservationSektor!!,
                    reservationOd!!,
                    reservationDo!!,
                    reservationKm!!,
                    reservationNamen!!
                )
            ) {
                dialog.dismiss()

                userViewModel.userLive.observe(this) { dbUser ->
                    if (dbUser == null) {
                        // create and insert new user
                        val firstName = reservationIzposojevalec.text.toString().split(" ")[0].lowercase()
                        val lastName = reservationIzposojevalec.text.toString().split(" ")[1].lowercase()
                        val sektor = reservationSektor.text.toString().lowercase()
                        userViewModel.insertUser(firstName, lastName, sektor)
                    }
                    // create and insert new reservation
                    val od = reservationOd.text.toString()
                    val doo = reservationDo.text.toString()
                    val km = reservationKm.progress.toString()
                    val namen = reservationNamen.text.toString()

                    userViewModel.userLive = MutableLiveData()
                }

                val firstName = reservationIzposojevalec.text.toString().split(" ")[0]
                val lastName = reservationIzposojevalec.text.toString().split(" ")[1]
                val sektor = reservationSektor.text.toString()

                userViewModel.getUser(firstName, lastName, sektor)

            } else {
                // TODO: show error message
            }
        }

        dataPopupView.findViewById<View>(R.id.reservationBackButton).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun insertReservation(userId: Long, bikeId: Long, reservationOd: String, reservationDo: String, prevozeniKm: Int, namen: String) {
        val reservation = Reservation(userId, bikeId, reservationOd, reservationDo, prevozeniKm, namen)
        reservationViewModel.insertReservation(reservation)
    }

    private fun checkInputs(vararg views: View): Boolean {
        var isValid = true
        for (view in views) {
            if (view is EditText) {
                if (view.text.isEmpty()) {
                    view.error = "Vnesite vrednost"
                    isValid = false
                }
            }
            if (view is SeekBar) {
                if (view.progress == 0) {
                    isValid = false
                }
            }
        }
        return isValid
    }
}