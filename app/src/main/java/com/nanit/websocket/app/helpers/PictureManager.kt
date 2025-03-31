package com.nanit.websocket.app.helpers

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class PictureManager(private val context: Context,private val dataStore: DataStore<Preferences>) : ImageStorage {

    companion object {
        private val IMAGE_PATH_KEY = stringPreferencesKey("image_path")
    }

    // Add a new image (removes the old one and saves the new one with a timestamp)
    override fun addImage(imageUri: Uri): String? {
        return try {
            // Generate a timestamped filename
            val timestamp = System.currentTimeMillis()
            val imageFile = File(context.filesDir, "stored_image_$timestamp.jpg")

            // Remove old image if it exists
            removeImage()

            // Save the new image
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            val outputStream: OutputStream = imageFile.outputStream()
            inputStream?.copyTo(outputStream)
            outputStream.close()
            inputStream?.close()

            // Store the new image path
            saveImagePath(imageFile.absolutePath)

            imageFile.absolutePath
        } catch (e: Exception) {
            null
        }
    }

    // Remove the stored image from internal storage
    override fun removeImage(): Boolean {
        val lastImagePath = getImage()
        return if (lastImagePath != null) {
            val file = File(lastImagePath)
            if (file.exists()) file.delete()
            saveImagePath(null)
            true
        } else {
            false
        }
    }

    // Retrieve the saved image path from DataStore
    override fun getImage(): String? {
        return runBlocking {
            dataStore.data.first()[IMAGE_PATH_KEY]
        }
    }

    // Save the image path to DataStore
    private fun saveImagePath(path: String?) {
        runBlocking {
            dataStore.edit { preferences ->
                if (path == null) {
                    preferences.remove(IMAGE_PATH_KEY)
                } else {
                    preferences[IMAGE_PATH_KEY] = path
                }
            }
        }
    }
}


interface ImageStorage {
    fun addImage(imageUri: Uri): String?
    fun removeImage(): Boolean
    fun getImage(): String?  // Added getImage method
}