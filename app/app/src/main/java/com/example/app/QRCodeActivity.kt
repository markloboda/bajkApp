package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.example.app.viewModel.StationViewModel
import com.google.common.util.concurrent.ListenableFuture
import com.learntodroid.androidqrcodescanner.QRCodeFoundListener
import java.util.concurrent.ExecutionException


class QRCodeActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CAMERA = 0

    private var requestCode: Int = -1
    private var qrCode: String = ""

    private lateinit var stationViewModel: StationViewModel

    private lateinit var previewView: PreviewView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_activity)
        stationViewModel = ViewModelProvider(this)[StationViewModel::class.java]
        requestCode = intent.getIntExtra("requestCode", -1)
        if (requestCode == -1) {
            Toast.makeText(this, "Error: No request code", Toast.LENGTH_SHORT).show()
            finish()
        }

        previewView = findViewById(R.id.previewView)

        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        requestCamera()
    }

    private fun requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        }
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

    private fun startCamera() {
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                bindCameraPreview(cameraProvider)
            } catch (e: ExecutionException) {
                Toast.makeText(this, "Error starting camera", Toast.LENGTH_SHORT).show()
            } catch (e: InterruptedException) {
                Toast.makeText(this, "Error starting camera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        val qrCodeHint = findViewById<View>(R.id.qrCodeHint)

        previewView.preferredImplementationMode = PreviewView.ImplementationMode.SURFACE_VIEW
        val preview: Preview = Preview.Builder()
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        preview.setSurfaceProvider(previewView.createSurfaceProvider())

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(700, 500))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), QRCodeImageAnalyzer(object : QRCodeFoundListener {
            override fun onQRCodeFound(_qrCode: String) {
                if (qrCode != _qrCode) {
                    qrCode = _qrCode
                    bikeRequest(qrCode)
                }
            }

            override fun qrCodeNotFound() {
                qrCodeHint.visibility = View.VISIBLE
            }
        }))

        val camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, imageAnalysis, preview)
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
                    Toast.makeText(this, "That parking spot is not available.", Toast.LENGTH_SHORT).show()
                    finish()
                } else if (spotStatus[spotIndex] == "0" && requestCode == 0) {
                    Toast.makeText(this, "There is no bike in that parking spot.", Toast.LENGTH_SHORT).show()
                    finish()
                }

                spotStatus[spotIndex] = requestCode.toString()
                station.spotStatus = spotStatus.joinToString(",")
                stationViewModel.updateStation(station)
            }
            finish()
        }

        stationViewModel.readStationById(stationId.toLong())
    }
}