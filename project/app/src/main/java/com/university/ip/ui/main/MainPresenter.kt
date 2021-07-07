package com.university.ip.ui.main

import MainContract
import android.content.Context
import com.university.ip.model.Photo
import com.university.ip.ui.base.BasePresenter
import com.university.ip.util.files.FileReaderLegacy

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun getAllPicturesFromFolder(context: Context): List<Photo> {

        //val picturesCollection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val fileReaderLegacy = FileReaderLegacy(context)
        return fileReaderLegacy.getFolderFiles()
    }
}