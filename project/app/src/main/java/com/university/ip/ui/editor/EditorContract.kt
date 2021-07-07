package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.ui.base.BaseContract

interface EditorContract {

    interface View : BaseContract.View {
        //view functions for each change of activity
        fun setBitmap(bitmap: Bitmap)
    }

    interface Presenter {
        //functions that are going to use our library
        fun brightness(bitmap: Bitmap, value: Int) : Bitmap

        fun contrast(bitmap: Bitmap, value: Int) : Bitmap

        fun grayscale(bitmap: Bitmap) : Bitmap

        fun binarization (bitmap: Bitmap, value: Int): Bitmap

        fun medianBlur(bitmap: Bitmap, value: Int): Bitmap

        fun gaussianBlur(bitmap: Bitmap, value: Int): Bitmap

        fun bilateralFilter(bitmap: Bitmap, value: Int): Bitmap

        fun rotate(bitmap: Bitmap, value: Int): Bitmap

        fun flip(bitmap: Bitmap): Bitmap

        fun adaptiveThresholding (bitmap: Bitmap, value: Int) : Bitmap

        fun zoomIn(bitmap: Bitmap, value: Int) : Bitmap

    }
}