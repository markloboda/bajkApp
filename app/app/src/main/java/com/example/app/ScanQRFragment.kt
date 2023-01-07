package com.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.TextView
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.learntodroid.androidqrcodescanner.QRCodeFoundListener


class ScanQRFragment : Fragment(R.layout.scan_qrcode_fragment) {


    private var qrCode: String = ""

    private lateinit var previewView: PreviewView
    private lateinit var qrCodeHint: View
    private lateinit var errorTextView: TextView
    private var errorFlag: Boolean = false

    private lateinit var requestActivity: RequestActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestActivity = activity as RequestActivity

        // Get views
        previewView = requireView().findViewById(R.id.previewView)
        qrCodeHint = requireView().findViewById<View>(R.id.qrCodeHint)
        errorTextView = requireView().findViewById(R.id.errorTextView)

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
                errorTextView.visibility = View.INVISIBLE
                qrCode = _qrCode
                requestActivity.bikeRequest(qrCode)
            }

            override fun qrCodeNotFound() {
                if (errorFlag) {
                    errorTextView.visibility = View.INVISIBLE
                    errorFlag = false
                }
                qrCodeHint.visibility = View.VISIBLE
            }
        }))

        val camera = cameraProvider.bindToLifecycle(requestActivity, cameraSelector, imageAnalysis, preview)
    }

    fun showRequestFail(msg: String) {
        // show message
        errorTextView.text = msg
        errorTextView.visibility = View.VISIBLE
        errorFlag = true
    }

}