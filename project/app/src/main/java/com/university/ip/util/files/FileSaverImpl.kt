package com.university.ip.util.files

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.university.ip.R
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaver.Companion.grantReadWriteAccessToFile
import java.io.File

@RequiresApi(Build.VERSION_CODES.Q)
class FileSaverImpl(private val context: Context) : FileSaver {

    override val cacheDir: File = context.cacheDir
    private val contentResolver = context.contentResolver
    private val folderName = context.getString(R.string.folder_name)

    override fun getFileUri(mimeType: String): Uri? {
        val uri = insertImage()
        uri?.let { grantReadWriteAccessToFile(it, context) }
        return uri
    }

    private fun insertImage(): Uri? {
        val picturesCollection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val pageDetails = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "${folderName}_${System.currentTimeMillis()}.jpg"
            )
            put(MediaStore.Images.Media.MIME_TYPE, IMAGE_MIME_TYPE)
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/$folderName")
            put(MediaStore.Images.Media.BUCKET_ID, folderName.hashCode())
            put(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, folderName)
            val currentTime = System.currentTimeMillis()
            put(MediaStore.Images.Media.DATE_ADDED, currentTime)
            put(MediaStore.Images.Media.DATE_TAKEN, currentTime)
        }
        return contentResolver.insert(picturesCollection, pageDetails)
    }
}