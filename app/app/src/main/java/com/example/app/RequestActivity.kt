package com.example.app

import android.content.pm.PackageManager
import android.graphics.drawable.TransitionDrawable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.app.viewModel.StationViewModel
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

class RequestActivity : AppCompatActivity() {

    val PERMISSION_REQUEST_CAMERA = 0

    private lateinit var stationViewModel: StationViewModel

    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private lateinit var colorFilterTransition: TransitionDrawable

    // fragments
    private lateinit var scanQRFragment: ScanQRFragment

    var requestCode: Int = -1

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_activity)

        stationViewModel = ViewModelProvider(this)[StationViewModel::class.java]
        scanQRFragment = ScanQRFragment()

        colorFilterTransition = getDrawable(R.drawable.request_failed_color_filter_transition) as TransitionDrawable

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, scanQRFragment)
            .addToBackStack(null)
            .commit()

        requestCode = intent.getIntExtra("requestCode", -1)
        if (requestCode == -1) {
            Toast.makeText(this, "Error: No request code", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    fun bikeRequest(qrCode: String) {
        val qrCodeSplit = qrCode.split(" ")
        if (qrCodeSplit[0] != "mbajkparking") {
            Toast.makeText(this, "QR code is not valid", Toast.LENGTH_SHORT).show()
            return
        }

        val stationId = qrCodeSplit[1].toLong()
        val spotIndex = qrCodeSplit[2].toInt()

        // Check if spot has bike
        stationViewModel.stationLive.observe(this) { station ->
            if (station != null) {
                val spotStatus = station.spotStatus.split(",").toMutableList()
                if (spotStatus[spotIndex] == "1" && requestCode == 1) {
                    showRequestFail("That parking spot is not available.")
                } else if (spotStatus[spotIndex] == "0" && requestCode == 0) {
                    showRequestFail("There is no bike in that parking spot.")
                } else {

                    spotStatus[spotIndex] = requestCode.toString()
                    station.spotStatus = spotStatus.joinToString(",")
                    stationViewModel.updateStation(station)

                    showRequestSuccessFragmentAndFinish(if (requestCode == 1) "Bike return successfully!" else "Bike unlocked!");
                }
            }
        }

        stationViewModel.readStationById(stationId.toLong())
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
