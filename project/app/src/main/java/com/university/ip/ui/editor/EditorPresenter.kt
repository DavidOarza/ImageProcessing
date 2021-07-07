package com.university.ip.ui.editor

import android.graphics.Bitmap
import com.university.ip.repository.Operators
import com.university.ip.ui.base.BasePresenter

class EditorPresenter : BasePresenter<EditorContract.View>(), EditorContract.Presenter {

    private val operators = Operators()

    override fun brightness(bitmap: Bitmap, value: Int) : Bitmap{
        val result = operators.increaseBrightness(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun contrast(bitmap: Bitmap, value: Int) : Bitmap{
        val result = operators.increaseContrast(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun grayscale(bitmap: Bitmap) : Bitmap{
        val result = operators.grayscale(bitmap)
        getView()?.setBitmap(result)
        return result
    }

    override fun binarization(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.increaseBinarization(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.medianBlur(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun gaussianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.gaussianBlur(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun bilateralFilter(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.bilateralFilter(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun rotate(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.rotate(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun flip(bitmap: Bitmap): Bitmap {
        val result = operators.flip(bitmap)
        getView()?.setBitmap(result)
        return result
    }

    override fun adaptiveThresholding(bitmap: Bitmap,value: Int): Bitmap {
        val result = operators.adaptiveThresholding(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }

    override fun zoomIn(bitmap: Bitmap, value: Int): Bitmap {
        val result = operators.zoomIn(bitmap, value)
        getView()?.setBitmap(result)
        return result
    }
}