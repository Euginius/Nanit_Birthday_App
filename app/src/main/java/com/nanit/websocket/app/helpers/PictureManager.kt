package com.nanit.websocket.app.helpers

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class PictureManager(private val context: Context) : ImageStorage {

    // Define a fixed file name for the image in internal storage
    private val imageFile: File = File(context.filesDir, "stored_image.jpg")

    // Add a new image to internal storage (replaces the existing image)
    override fun addImage(imageUri: Uri): String? {
        return try {
            // Open an InputStream to read the image from the URI
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)

            // Open an OutputStream to write the image to internal storage
            val outputStream: OutputStream = context.openFileOutput(imageFile.name, Context.MODE_PRIVATE)

            // Copy the image data from the InputStream to the OutputStream (this will replace the existing image)
            inputStream?.copyTo(outputStream)

            // Close the streams
            outputStream.close()
            inputStream?.close()

            // Return the saved image file
            imageFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    // Remove the stored image from internal storage
    override fun removeImage(): Boolean {
        return imageFile.exists() && imageFile.delete()
    }

    // Retrieve the saved image from internal storage
    override fun getImage(): String? {
        return if (imageFile.exists()) imageFile.absolutePath else null
    }
}


interface ImageStorage {
    fun addImage(imageUri: Uri): String?
    fun removeImage(): Boolean
    fun getImage(): String?  // Added getImage method
}