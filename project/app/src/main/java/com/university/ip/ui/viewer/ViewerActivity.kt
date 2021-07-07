package com.university.ip.ui.viewer

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import com.university.ip.R
import com.university.ip.model.Photo
import com.university.ip.ui.main.MainActivity


class ViewerActivity : AppCompatActivity(), ViewerContract.View, View.OnClickListener {

    override fun appContext(): Context = applicationContext

    private lateinit var presenter: ViewerPresenter
    private lateinit var imageView: PhotoView
    private lateinit var backButton: ImageView
    private lateinit var photo: Photo

    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewer)

        imageView = findViewById(R.id.image_viewer)
        backButton = findViewById(R.id.back_viewer)
        backButton.setOnClickListener(this)

        photo = Photo(
            name = intent.getStringExtra(PHOTO_NAME)!!,
            path = intent.getStringExtra(PHOTO_PATH)!!
        )
        val bitmap = BitmapFactory.decodeFile(photo.path)
        imageView.setImageBitmap(bitmap)
    }

    override fun onClick(v: View?) {
        startActivity(Intent(appContext(), MainActivity::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

    companion object {
        const val PHOTO_PATH: String = "PHOTO_PATH"
        const val PHOTO_NAME: String = "PHOTO_NAME"
    }
}