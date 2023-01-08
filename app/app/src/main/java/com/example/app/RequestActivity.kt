package com.example.app

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.app.data.user.User
import com.example.app.viewModel.BikeViewModel
import com.example.app.viewModel.StationViewModel
import com.example.app.viewModel.UserViewModel
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

class RequestActivity : AppCompatActivity() {

    val PERMISSION_REQUEST_CAMERA = 0

    private var qrCodeCount = 0

    private lateinit var stationViewModel: StationViewModel
    private lateinit var bikeViewModel: BikeViewModel
    private lateinit var userViewModel: UserViewModel

    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    // fragments
    private lateinit var scanQRFragment: ScanQRFragment

    var requestCode: Int = -1
    lateinit var user: User

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_activity)

        stationViewModel = ViewModelProvider(this)[StationViewModel::class.java]
        bikeViewModel = ViewModelProvider(this)[BikeViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]


        requestCode = intent.getIntExtra("requestCode", -1)
        if (requestCode == -1) {
            Toast.makeText(this, "Error: No request code", Toast.LENGTH_SHORT).show()
            finish()
        }

        // get user
        val phone = intent.getStringExtra("phone")!!
        userViewModel.userByPhone.observe(this) { user ->
            if (user != null) {
                this.user = user
            } else {
                Toast.makeText(this, "Error: User not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        userViewModel.getUserByPhone(phone)

        scanQRFragment = ScanQRFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, scanQRFragment)
            .addToBackStack(null)
            .commit()
    }

    fun bikeRequest(qrCode: String) {
        val qrCodeSplit = qrCode.split(" ")
        if (qrCodeSplit[0] != "mbajkparking") {
            Toast.makeText(this, "QR code is not valid", Toast.LENGTH_SHORT).show()
            return
        }

        val stationId = qrCodeSplit[1].toLong()
        val spotIndex = qrCodeSplit[2].toInt()

        // Get the bike (if any) at the spot
        bikeViewModel.bikeByStationIdAndSpotIndex.observe(this) { bike ->
            if (bike == null && requestCode == 1) {
                //
                // Return bike
                //

                // Get the bike from the user
                val bikeId = user.bikeId

                // Update the bike
                bikeViewModel.updateBike(bikeId, stationId, spotIndex)

                // Update the user
                userViewModel.updateUserBike(user.id, -1)

                // Response to user
                showRequestSuccessFragmentAndFinish("Bike returned successfully!")
            } else if (bike != null && requestCode == 0) {
                //
                // Take bike
                //

                // Update the bike
                bikeViewModel.updateBike(bike.id, -1, -1)

                // Update the user
                userViewModel.updateUserBike(user.id, bike.id)

                // Response to user
                showRequestSuccessFragmentAndFinish("Bike unlocked!")
            } else {
                // INVALID REQUEST
                if (requestCode == 0) {
                    showRequestFail("Bike already taken!")
                } else if (requestCode == 1) {
                    showRequestFail("Parking spot is taken!")
                }
            }
        }
        bikeViewModel.readBikeByStationIdAndSpotIndex(stationId, spotIndex)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startCamera() {
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                scanQRFragment.bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                Toast.makeText(this, "Error starting camera", Toast.LENGTH_SHORT).show()
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Error starting camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun showRequestSuccessFragmentAndFinish(text: String) {
        val requestSuccessFragment = RequestSuccessFragment()
        val bundle = android.os.Bundle()
        bundle.putString("text", text)
        requestSuccessFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, requestSuccessFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showRequestFail(text: String) {
        scanQRFragment.showRequestFail(text)
    }
}
