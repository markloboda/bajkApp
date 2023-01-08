package com.learntodroid.androidqrcodescanner

interface QRCodeFoundListener {
    fun onQRCodeFound(_qrCode: String)
    fun qrCodeNotFound()
}