package com.university.ip.util.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.university.ip.model.Photo
import java.io.File

interface FileReader {
    val cacheDir: File
    fun getFiles(activity: Activity): List<Photo>
    fun getFolderFiles(): List<Photo>

    companion object {
        const val IMAGE_MIME_TYPE = "image/jpeg"

        private const val provider = "com.android.providers.media.MediaProvider"

        @JvmStatic
        fun grantReadWriteAccessToFile(uri: Uri, context: Context) {
            // grant all three uri permissions!
            context.apply {
                grantUriPermission(provider, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                grantUriPermission(provider, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                grantUriPermission(provider, uri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
            }
        }
    }
}