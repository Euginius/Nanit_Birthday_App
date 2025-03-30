package com.nanit.websocket.app.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ShareUtils {

    fun shareView(context: Context,view: View) {
        val bitmap = captureComposableAsBitmap(view)
        val uri = saveBitmapToFile(context, bitmap)
        uri?.let { shareImage(context, it) }
    }

    // Function to capture a Composable and return a Bitmap
    private fun captureComposableAsBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    // Function to save Bitmap to a file and return URI
    private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri? {
        val filename = "shared_image_${System.currentTimeMillis()}.jpg"
        val file = File(context.cacheDir, filename)
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
        }

        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    // Function to share an image via Intent
    private fun shareImage(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/jpeg"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Image"))
    }
}