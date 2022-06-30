package com.example.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
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

    fun dataDialog(bikeId: Int, bikeTitle: String, bikeStatus: Boolean) {
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
                updateBikeStatus(bikeId, false)

                userViewModel.userId.observe(this) { userId ->
                    insertReservation(
                        userId,
                        bikeId,
                        reservationKm.progress,
                        reservationNamen.text.toString()
                    )
                }
                insertUserGetId(
                    reservationIzposojevalec.text.toString(),
                    reservationSektor.text.toString(),
                    reservationKm.progress,
                )
            } else {
                // TODO: show error message
            }
        }

        dataPopupView.findViewById<View>(R.id.reservationBackButton).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun insertUser(izposojevalec: String, sektor: String, km: Int) {
        val firstName = izposojevalec.split(" ")[0]
        val lastName = izposojevalec.split(" ")[1]
        val user = User(firstName, lastName, sektor)
        userViewModel.insertUser(user)
    }

    /**
     * insert into database and returns userId
     * @return userId
     */
    private fun insertUserGetId(izposojevalec: String, sektor: String, km: Int) {
        val firstName = izposojevalec.split(" ")[0]
        val lastName = izposojevalec.split(" ")[1]

        val user = User(firstName, lastName, sektor)
        userViewModel.insertUserGetId(user)
    }

    private fun insertReservation(userId: Int, bikeId: Int, prevozeniKm: Int, namen: String) {
        val reservation = Reservation(userId, bikeId, prevozeniKm, namen)
        reservationViewModel.insertReservation(reservation)
    }

    private fun updateBikeStatus(bikeId: Int, bikeStatus: Boolean) {
//        bikeViewModel.updateBikeStatus(bikeId, bikeStatus)
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