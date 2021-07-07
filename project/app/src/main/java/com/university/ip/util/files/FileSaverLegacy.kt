package com.university.ip.util.files

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.university.ip.R
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import java.io.File

@Suppress("DEPRECATION")
class FileSaverLegacy(private val context: Context) : FileSaver {

    override val cacheDir: File = context.cacheDir
    private val contentResolver = context.contentResolver
    private val folderName = context.getString(R.string.folder_name)

    override fun getFileUri(mimeType: String): Uri? {
        val storage =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).also {
                if (!it.exists()) {
                    it.mkdir()
                }
            }
        val appFolder = File(storage, folderName).also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }
        val uri = insertImage(appFolder)
        uri?.let { FileSaver.grantReadWriteAccessToFile(it, context) }
        return uri
    }

    private fun insertImage(appFolder: File): Uri? {
        val pageFile = File(
            appFolder, "${folderName}_${System.currentTimeMillis()}.jpg"
        )
        val dateAdded = System.currentTimeMillis()

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, dateAdded.toString())
        values.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "${folderName}_${System.currentTimeMillis()}.jpg"
        )
        values.put(MediaStore.Images.Media.MIME_TYPE, IMAGE_MIME_TYPE)

        // Add the date meta data to ensure the image is added at the front of the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, dateAdded)
        values.put(MediaStore.Images.Media.DATA, pageFile.path)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
}
