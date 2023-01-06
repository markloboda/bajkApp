package com.example.app

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutionException

class RequestActivity : AppCompatActivity() {

    val PERMISSION_REQUEST_CAMERA = 0

    lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    // fragments
    private lateinit var scanQRFragment: ScanQRFragment

    var requestCode: Int = -1

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_activity)

        scanQRFragment = ScanQRFragment()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainerView2, scanQRFragment)
            .addToBackStack(null)
            .commit()

        requestCode = intent.getIntExtra("requestCode", -1)
        if (requestCode == -1) {
            Toast.makeText(this, "Error: No request code", Toast.LENGTH_SHORT).show()
            finish()
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

}
