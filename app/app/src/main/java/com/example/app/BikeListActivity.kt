package com.example.app

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.app.data.reservation.Reservation
import com.example.app.recyclerView.BikesRecyclerViewAdapter
import com.example.app.viewModel.BikeViewModel
import com.example.app.viewModel.LocationViewModel
import com.example.app.viewModel.ReservationViewModel
import com.example.app.viewModel.UserViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class BikeListActivity : AppCompatActivity() {

    private lateinit var dialogBuilder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog

    private lateinit var bikeViewModel: BikeViewModel
    private lateinit var locationViewMode: LocationViewModel
    private lateinit var reservationViewModel: ReservationViewModel
    private lateinit var userViewModel: UserViewModel


    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("d/M/yyyy-H:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bike_list_activity)

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

        reservationViewModel = ViewModelProvider(this)[ReservationViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // setup swipe refresh
        val swipeRefresh = findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            refreshBikes()
            swipeRefresh.isRefreshing = false
        }
    }

    @SuppressLint("SetTextI18n")
    fun bikeDataDialog(bikeId: Long) {
        // get bike data and set it to the dialog
        bikeViewModel.bikeLive.observe(this) { bike ->
            bike!!
            dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setTitle("Informacije o ${bike.title}")
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.bike_dialog, null)
            dialogBuilder.setView(dialogView)
            dialog = dialogBuilder.create()
            dialog.show()

            // get bike reservations
            reservationViewModel.reservationLive.observe(this) { reservations ->
                reservations!!

                // find previous reservation for this bike
                val currentDate = Calendar.getInstance().timeInMillis
                var previousReservation: Reservation? = null
                var nextReservation: Reservation? = null
                for (reservation in reservations) {
                    if (currentDate < reservation!!.startTime) {
                        nextReservation = reservation
                        break
                    }
                    previousReservation = reservation
                }
                if (previousReservation != null) {
                    dialog.findViewById<TextView>(R.id.bikeLastReservation)?.text =
                        "${getDate(previousReservation.startTime)}"
                } else {
                    dialog.findViewById<TextView>(R.id.bikeLastReservation)?.text =
                        "Nima rezervacij"
                }
                if (nextReservation != null) {
                    dialog.findViewById<TextView>(R.id.bikeNextReservation)?.text =
                        "${getDate(nextReservation.startTime)}"
                } else {
                    dialog.findViewById<TextView>(R.id.bikeNextReservation)?.text =
                        "Nima rezervacij"
                }

                reservationViewModel.reservationLive = MutableLiveData()
            }
            reservationViewModel.getAllBikeReservations(bikeId)

            dialog.findViewById<Button>(R.id.buttonClose)?.setOnClickListener {
                dialog.dismiss()
            }

            dialog.findViewById<Button>(R.id.buttonRezerviraj)?.setOnClickListener {
                dialog.dismiss()
                reservationDataDialog(bike.id, bike.title, bike.status)
            }

            bikeViewModel.bikeLive = MutableLiveData()
        }
        bikeViewModel.readBikeById(bikeId)
    }

    fun reservationDataDialog(bikeId: Long, bikeTitle: String, bikeStatus: Boolean) {
        /*
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
            val reservationSektor = dialog.findViewById<Spinner>(R.id.reservationSektor)
            val reservationOdDate = dialog.findViewById<EditText>(R.id.reservationOdDate)
            val reservationOdTime = dialog.findViewById<EditText>(R.id.reservationOdTime)
            val reservationDoDate = dialog.findViewById<EditText>(R.id.reservationDoDate)
            val reservationDoTime = dialog.findViewById<EditText>(R.id.reservationDoTime)
            val reservationKm = dialog.findViewById<SeekBar>(R.id.reservationKm)
            val reservationNamen = dialog.findViewById<Spinner>(R.id.reservationNamen)

            // setup dialog buttons
            dataPopupView.findViewById<View>(R.id.reservationConfirmButton).setOnClickListener {
                if (checkInputs(
                        reservationIzposojevalec!!,
                        reservationSektor!!,
                        reservationOdDate!!,
                        reservationOdTime!!,
                        reservationDoDate!!,
                        reservationDoTime!!,
                        reservationKm!!,
                        reservationNamen!!
                    )
                ) {
                    dialog.dismiss()

                    userViewModel.userLive.observe(this) { dbUser ->
                        // convert date to long
                        val startTime =
                            dateFormat.parse("${reservationOdDate.text}-${reservationOdTime.text}")
                        val endTime =
                            dateFormat.parse("${reservationDoDate.text}-${reservationDoTime.text}")

                        // if user already exists, insert reservation
                        // else if user doesnt exist, create user and wait for insertion, then get id and insert reservation
                        if (dbUser == null) {
                            // create and insert new user
                            val firstName =
                                reservationIzposojevalec.text.toString().split(" ")[0].lowercase()
                            val lastName =
                                reservationIzposojevalec.text.toString().split(" ")[1].lowercase()
                            val sektor = reservationSektor.selectedItem.toString().lowercase()
                            userViewModel.insertUser(firstName, lastName, sektor)
                            userViewModel.userIdLive.observe(this) { userId ->
                                // create and insert new reservation
                                insertReservation(
                                    userId,
                                    bikeId,
                                    startTime!!.time,
                                    endTime!!.time,
                                )
                                userViewModel.userIdLive = MutableLiveData()
                                userViewModel.userIdLive.removeObservers(this)
                            }
                        } else {
                            // create and insert new reservation
                            insertReservation(dbUser.id, bikeId)
                        }

                        // update bike km
                        bikeViewModel.bikeLive.observe(this) { bike ->
                            bikeViewModel.update(bike)
                            bikeViewModel.bikeLive = MutableLiveData()
                            bikeViewModel.bikeLive.removeObservers(this)
                        }
                        bikeViewModel.readBikeById(bikeId)

                        userViewModel.userLive = MutableLiveData()
                        userViewModel.userLive.removeObservers(this)
                    }

                    val firstName = reservationIzposojevalec.text.toString().split(" ")[0]
                    val lastName = reservationIzposojevalec.text.toString().split(" ")[1]
                    val sektor = reservationSektor.selectedItem.toString()

                    userViewModel.getUser(firstName, lastName, sektor)
                } else {
                    // TODO: show error message
                }
            }
            // setup spinners
            setupSpinnerAdapter(reservationSektor!!, resources.getStringArray(R.array.sektor))
            setupSpinnerAdapter(reservationNamen!!, resources.getStringArray(R.array.namen))

            dataPopupView.findViewById<View>(R.id.reservationBackButton).setOnClickListener {
                dialog.dismiss()
            }
        */
    }

    private fun insertReservation(
        userId: Long,
        bikeId: Long,
        startLocationId: Long,
        endLocationId: Long,
        startTime: Long,
        endTime: Long
    ) {
        val reservation =
            Reservation(userId, bikeId, startLocationId, endLocationId, startTime, endTime)
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

    private fun setupSpinnerAdapter(spinner: Spinner, items: Array<String>) {
        val spinnerAdapter = object : ArrayAdapter<String>(this, R.layout.spinner_row, items) {
            override fun isEnabled(position: Int): Boolean {
                //first item is hint, disable
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val textView = view as TextView
                if (position == 0) {
                    // set color of hint to gray
                    textView.setTextColor(Color.GRAY)
                } else {
                    textView.setTextColor(Color.BLACK)
                }
                return view
            }
        }
        spinner.adapter = spinnerAdapter
        // listener to chanage color depending on selected item (if hint it is gray, if not black)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = parent!!.getItemAtPosition(position).toString()
                if (value == items[0]) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
            }
        }
    }

    private fun getDate(milliSeconds: Long): String? {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return dateFormat.format(calendar.time)
    }

    private fun refreshBikes() {
        // check bike availability
        // get reservations where now is between start and end date
        reservationViewModel.reservationLive.observe(this) { reservations ->
            val bikeIds = ArrayList<Long>(reservations.size)
            for (reservation in reservations) {
                bikeIds.add(reservation!!.bikeId)
            }
            // reset all bike availability to true
            bikeViewModel.resetAllBikesAvailability()
            // set bikes availability to false where bikeId is in bikeIds
            bikeViewModel.setBikeAvailabilityFalse(bikeIds)

            reservationViewModel.reservationLive = MutableLiveData()
        }
        reservationViewModel.getAllActiveReservations()
    }
}
