package com.university.ip.repository

import android.graphics.Bitmap
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class Operators {
    fun increaseBrightness(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, 1.0, value.toDouble())
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseContrast(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        src.convertTo(src, -1, value.toDouble()/50, 1.0)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun grayscale(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY);
        Imgproc.cvtColor(src, src, Imgproc.COLOR_GRAY2RGB, 4);

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun increaseBinarization(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)
        Imgproc.threshold(src, src, value.toDouble(), 255.0, Imgproc.THRESH_BINARY);
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun medianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        if (value % 2 == 0)
            Imgproc.medianBlur(src, src, value + 1)
        else
            Imgproc.medianBlur(src, src, value + 2)
        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun gaussianBlur(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        if(value % 2 != 0)
            Imgproc.GaussianBlur(src, src, Size(value.toDouble(), value.toDouble()), 0.0, 0.0)
        else
            Imgproc.GaussianBlur(
                    src,
                    src,
                    Size(value.toDouble() + 1.0, value.toDouble() + 1.0),
                    0.0,
                    0.0
            )

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun bilateralFilter(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2RGB, 0)
        val dst = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, dst)
        Imgproc.bilateralFilter(src, dst, value, value.toDouble(), value.toDouble(), Core.BORDER_DEFAULT)

        val result = Bitmap.createBitmap(dst.cols(), dst.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(dst, result)
        return result
    }

    fun rotate(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Core.transpose(src, src)
        Core.flip(src, src, value)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun flip(bitmap: Bitmap): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Core.flip(src, src, 1)

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun adaptiveThresholding(bitmap: Bitmap, value: Int): Bitmap {

        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGB2GRAY)

        Imgproc.adaptiveThreshold(src, src, 255.0, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, value.toDouble());

        val result = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(src, result)
        return result
    }

    fun zoomIn(bitmap: Bitmap, value: Int): Bitmap {
        val src = Mat(bitmap.getHeight(), bitmap.getWidth(), CvType.CV_8UC1)
        Utils.bitmapToMat(bitmap, src)

        Imgproc.resize(src, src, Size((value*5 + src.cols()).toDouble(), (value*5 + src.rows()).toDouble()))
        val roi = Rect(0, 0, bitmap.width, bitmap.height)
        val cropped = Mat(src, roi)

        val result = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(cropped, result)
        return result
    }
}