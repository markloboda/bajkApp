package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.app.viewModel.StationViewModel
import com.learntodroid.androidqrcodescanner.QRCodeFoundListener


class ScanQRFragment : Fragment(R.layout.scan_qrcode_fragment) {


    private var qrCode: String = ""

    private lateinit var stationViewModel: StationViewModel

    private lateinit var previewView: PreviewView
    private lateinit var qrCodeHint: View

    private lateinit var requestActivity: RequestActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stationViewModel = ViewModelProvider(this)[StationViewModel::class.java]
        requestActivity = activity as RequestActivity

        // Get views
        previewView = requireView().findViewById(R.id.previewView)
        qrCodeHint = requireView().findViewById<View>(R.id.qrCodeHint)



        requestActivity.cameraProviderFuture = ProcessCameraProvider.getInstance(requestActivity)
        requestCamera()
    }

    private fun requestCamera() {
        if (ActivityCompat.checkSelfPermission(requestActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            requestActivity.startCamera()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requestActivity, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(requestActivity, arrayOf(Manifest.permission.CAMERA), requestActivity.PERMISSION_REQUEST_CAMERA)
            } else {
                ActivityCompat.requestPermissions(requestActivity, arrayOf(Manifest.permission.CAMERA), requestActivity.PERMISSION_REQUEST_CAMERA)
            }
        }
    }

    fun bindCameraPreview(cameraProvider: ProcessCameraProvider) {
        previewView.preferredImplementationMode = PreviewView.ImplementationMode.SURFACE_VIEW
        val preview: Preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        preview.setSurfaceProvider(previewView.createSurfaceProvider())

        val imageAnalysis = ImageAnalysis.Builder().setTargetResolution(Size(700, 500)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requestActivity), QRCodeImageAnalyzer(object : QRCodeFoundListener {
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

        val camera = cameraProvider.bindToLifecycle(requestActivity, cameraSelector, imageAnalysis, preview)
    }

    fun bikeRequest(qrCode: String) {
        val qrCodeSplit = qrCode.split(" ")
        if (qrCodeSplit[0] != "mbajkparking") {
            Toast.makeText(requestActivity, "QR code is not valid", Toast.LENGTH_SHORT).show()
            return
        }

        val stationId = qrCodeSplit[1].toLong()
        val spotIndex = qrCodeSplit[2].toInt()

        // Check if spot has bike
        stationViewModel.stationLive.observe(this) { station ->
            if (station != null) {
                val spotStatus = station.spotStatus.split(",").toMutableList()
                if (spotStatus[spotIndex] == "1" && requestActivity.requestCode == 1) {
                    Toast.makeText(requestActivity, "That parking spot is not available.", Toast.LENGTH_SHORT).show()
                    requestActivity.finish()
                } else if (spotStatus[spotIndex] == "0" && requestActivity.requestCode == 0) {
                    Toast.makeText(requestActivity, "There is no bike in that parking spot.", Toast.LENGTH_SHORT).show()
                    requestActivity.finish()
                }

                spotStatus[spotIndex] = requestActivity.requestCode.toString()
                station.spotStatus = spotStatus.joinToString(",")
                stationViewModel.updateStation(station)
            }
            requestActivity.finish()
        }

        stationViewModel.readStationById(stationId.toLong())
    }
}