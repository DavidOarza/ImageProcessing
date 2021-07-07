package com.university.ip.ui.editor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.ui.main.MainActivity
import com.university.ip.util.files.FileSaver.Companion.IMAGE_MIME_TYPE
import com.university.ip.util.files.FileSaverLegacy
import org.opencv.android.OpenCVLoader

class EditorActivity : AppCompatActivity(), EditorContract.View, View.OnClickListener,
    FiltersAdapter.ItemClickListener, SeekBar.OnSeekBarChangeListener {

    override fun appContext(): Context = applicationContext
    private val TAG = "EditorActivity"

    private lateinit var backButton: ImageView
    private lateinit var saveButton: ImageView
    private lateinit var imageView: ImageView
    private lateinit var filterList: RecyclerView
    private lateinit var seekBar: SeekBar
    private var grayscaleApplied = false

    private lateinit var adapter: FiltersAdapter

    private lateinit var fileSaver: FileSaverLegacy
    private lateinit var bitmap: Bitmap
    private lateinit var initialBitmap : Bitmap
    private lateinit var bitmapResult: Bitmap
    private lateinit var selectedFilter: String
    private lateinit var presenter: EditorPresenter

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        //list initialization
        val layoutManager = LinearLayoutManager(appContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = findViewById(R.id.filters_list)
        filterList.layoutManager = layoutManager
        adapter = FiltersAdapter(appContext(), this)
        adapter.setMediaList(FILTERS_ARRAY);
        filterList.adapter = adapter

        seekBar = findViewById(R.id.seek_bar_editor)

        backButton = findViewById(R.id.back_editor)
        backButton.setOnClickListener(this)

        imageView = findViewById(R.id.image_edited)

        fileSaver = FileSaverLegacy(appContext())
        saveButton = findViewById(R.id.save_button)
        saveButton.setOnClickListener(this)
        presenter = EditorPresenter()
        presenter.bindView(this)
        //image load
        loadImage()
        openCvInit()

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun openCvInit() {
        if (!OpenCVLoader.initDebug()) {
            Log.e(TAG, "OpenCV not loaded");
        } else {
            Log.e(TAG, "OpenCV loaded");
        }
    }

    private fun loadImage() {
        val data = intent.getBundleExtra(INTENT_EXTRAS)
        val requestCode = intent.getIntExtra(REQUEST_CODE, 2)
        val resultCode = intent.getIntExtra(RESULT_CODE, Activity.RESULT_CANCELED)

        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = data.get("data") as Bitmap
                    bitmap = selectedImage
                    bitmapResult = bitmap
                    initialBitmap = bitmap
                    imageView.setImageBitmap(selectedImage)
                }
                1 -> if (resultCode == Activity.RESULT_OK && intent != null) {
                    val selectedImage = intent.data!!
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = contentResolver.query(
                        selectedImage,
                        filePathColumn, null, null, null
                    )
                    if (cursor != null) {
                        cursor.moveToFirst()

                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val picturePath = cursor.getString(columnIndex)
                        bitmap = BitmapFactory.decodeFile(picturePath)
                        bitmapResult = bitmap
                        initialBitmap = bitmap
                        imageView.setImageBitmap(bitmap)
                        cursor.close()
                    }

                }
            }
        }
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        val FILTERS_ARRAY: List<String> = listOf("Brightness", "Contrast",  "Binarization", "Median blur", "Gaussian blur", "Bilateral filter", "Adaptive thresholding", "Zoom", "Grayscale", "Rotate right", "Rotate left", "Flip", "Reset")
        val FILTERS_SLIDER_ARRAY: List<String> = listOf("Brightness", "Contrast", "Binarization", "Median blur", "Gaussian blur", "Bilateral filter", "Adaptive thresholding", "Zoom")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.back_editor -> {
                finish()
            }
            R.id.save_button -> {
                val uri = fileSaver.getFileUri(IMAGE_MIME_TYPE) ?: return
                appContext().contentResolver.openOutputStream(uri)?.use { stream ->
                    bitmapResult.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                }
                startActivity(Intent(appContext(), MainActivity::class.java))
            }
        }
    }

    override fun onItemClick(filter: String) {
        selectedFilter = filter
        bitmap = bitmapResult
        if (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter) >= 0) {
            seekBar.visibility = View.VISIBLE
            seekBar.setOnSeekBarChangeListener(this)
        } else {
            seekBar.visibility = View.GONE
            println(selectedFilter)
            when(FILTERS_ARRAY.indexOf(selectedFilter)) {
                8 -> {
                    if(!grayscaleApplied)
                    {
                        bitmapResult = presenter.grayscale(bitmap)
                        grayscaleApplied = true
                    }
                    return
                }

                9 -> {
                    bitmapResult = presenter.rotate(bitmap, 1)
                }

                10 -> {
                    bitmapResult = presenter.rotate(bitmap, 0)
                }

                11 -> {
                    bitmapResult = presenter.flip(bitmap)
                }

                12 -> {
                    bitmapResult = initialBitmap
                    bitmap = initialBitmap
                    setBitmap(bitmap)
                    grayscaleApplied = false
                }
            }
        }
        println(FILTERS_SLIDER_ARRAY.indexOf(selectedFilter))
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        println(progress)
        println(selectedFilter)
        when (FILTERS_SLIDER_ARRAY.indexOf(selectedFilter)) {
            0 -> {
                bitmapResult = presenter.brightness(bitmap, progress)
                seekBar.max = 255
                return
            }
            1 -> {
                bitmapResult = presenter.contrast(bitmap, progress)
                seekBar.max = 255
                return
            }
            2 -> {
                bitmapResult = presenter.binarization(bitmap, progress)
                seekBar.max = 255
                return
            }
            3 -> {
                bitmapResult = presenter.medianBlur(bitmap, progress)
                seekBar.max = 50
                return
            }
            4 -> {
                bitmapResult = presenter.gaussianBlur(bitmap, progress)
                seekBar.max = 50
                return
            }
            5 -> {
                bitmapResult = presenter.bilateralFilter(bitmap, progress)
                seekBar.max = 255
                return
            }
            6 -> {
                bitmapResult = presenter.adaptiveThresholding(bitmap, progress)
                seekBar.max = 255
                return
            }
            7 -> {
                bitmapResult = presenter.zoomIn(bitmap, progress)
                seekBar.max = 255
                return
            }
            else -> return
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}

    override fun onStopTrackingTouch(seekBar: SeekBar) {}

    override fun setBitmap(bitmap: Bitmap) {
        imageView.setImageBitmap(bitmap)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }
}