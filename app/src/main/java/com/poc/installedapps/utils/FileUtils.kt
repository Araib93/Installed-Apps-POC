package com.poc.installedapps.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileUtils constructor(private val context: Context) {
    fun getFile(name: String, format: String, path: String): File {
        val root = context.filesDir
        val myDir = File("$root/$path")
        myDir.mkdirs()
        val fname = "$name.$format"
        val file = File(myDir, fname)
        return file
    }

    fun getTempFile(name: String, extension: String, path: String): File {
        val root = context.filesDir
        val myDir = File("$root/$path")
        myDir.mkdirs()
        val file = File.createTempFile(name, extension)
        return file
    }

    fun fileExists(name: String, format: String, path: String): Boolean {
        val file = getFile(name, format, path)
        return file.exists()
    }

    fun deleteFile(name: String, format: String, path: String): Boolean {
        val file = getFile(name, format, path)
        return file.delete()
    }

    suspend fun saveBitmap(name: String, bitmap: Bitmap?): Uri =
        withContext(Dispatchers.IO) {
            val file = getFile(name, "jpeg", "app_icons")
            file.outputStream().use { fos ->
                bitmap?.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.flush()
            }
            Uri.fromFile(file)
        }
}