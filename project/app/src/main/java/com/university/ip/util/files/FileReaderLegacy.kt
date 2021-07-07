package com.university.ip.util.files

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.university.ip.R
import com.university.ip.model.Photo
import java.io.File

@Suppress("DEPRECATION")
class FileReaderLegacy(private val context: Context) : FileReader {

    override val cacheDir: File = context.cacheDir
    //private val contentResolver = context.contentResolver
    private val folderName = context.getString(R.string.folder_name)

    override fun getFolderFiles(): List<Photo> {
        var uris = ArrayList<Photo>()
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
        FileSaver.grantReadWriteAccessToFile(appFolder.absolutePath.toUri(), context)
        val files = appFolder.listFiles()
        for (it in files) {
            val photo = Photo(it.path, it.name)
            uris.add(photo)
        }
        //uri?.let { FileSaver.grantReadWriteAccessToFile(it, context) }
        return uris
    }

    override fun getFiles(activity: Activity): List<Photo> {
        var photos = ArrayList<Photo>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED
        )
        var cursor = activity.contentResolver.query(
            uri,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " DESC"
        )
        val columnIndexData = cursor!!.getColumnIndex(MediaStore.MediaColumns.DATA)
        val columnIndexTitle = cursor!!.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
        //val columnIndexFolderName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        while (cursor.moveToNext()) {
            var absolutePathOfImage = ""
            var title = ""
            if (columnIndexData >= 0) {
                absolutePathOfImage = cursor.getString(columnIndexData)
            }
            if (columnIndexTitle >= 0) {
                title = cursor.getString(columnIndexTitle)
            }
            photos.add(Photo(absolutePathOfImage, title))
        }
        return photos
    }

}
