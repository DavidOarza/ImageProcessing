package com.university.ip.ui.main

import MainContract
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.university.ip.R
import com.university.ip.model.Photo
import com.university.ip.ui.editor.EditorActivity
import com.university.ip.ui.viewer.ViewerActivity


class MainActivity : AppCompatActivity(), MainContract.View, View.OnClickListener,
    PhotosDisplayAdapter.ItemClickListener {

    override fun appContext(): Context = applicationContext

    private lateinit var photosRecycler: RecyclerView
    private lateinit var noPhotosText: TextView
    private lateinit var addButton: FloatingActionButton
    private lateinit var presenter: MainPresenter
    private lateinit var adapter: PhotosDisplayAdapter
    private var photoList: List<Photo>? = null

    /** Called when the activity is first created. */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photosRecycler = findViewById(R.id.photos_display)
        photosRecycler.layoutManager = GridLayoutManager(appContext(), 2)


        noPhotosText = findViewById(R.id.no_photos_text)
        addButton = findViewById(R.id.add_button)
        addButton.setOnClickListener(this)

        presenter = MainPresenter()
        presenter.bindView(this)

        adapter = PhotosDisplayAdapter(appContext(), this)
        photosRecycler.adapter = adapter

        photoList =
            presenter.getAllPicturesFromFolder(appContext()) // - this is where we initialize the photoList
        adapter.setMediaList(photoList!!)

        toggleVisibility()
    }

    private fun toggleVisibility() {
        if (photoList!!.isEmpty()) {
            noPhotosText.visibility = View.VISIBLE
            photosRecycler.visibility = View.GONE
        } else {
            noPhotosText.visibility = View.GONE
            photosRecycler.visibility = View.VISIBLE
        }
    }

    //activity result after
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_CANCELED) {
            val intent = Intent(this, EditorActivity::class.java).apply {
                this.putExtra(INTENT_EXTRAS, data!!.extras)
                this.putExtra(REQUEST_CODE, requestCode)
                this.putExtra(RESULT_CODE, resultCode)
                this.data = data.data
            }
            startActivity(intent)
        }
    }

    //function for selecting camera or image collection
    private fun selectImage(context: Context) {
        val options = arrayOf("Camera", "Gallery", "Cancel")

        val builder = AlertDialog.Builder(context).setItems(options) { dialog, item ->
            when (item) {
                0 -> {
                    val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(takePicture, 0)

                }
                1 -> {
                    val pickPhoto = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(pickPhoto, 1)

                }
                3 -> dialog.dismiss()
            }
        }
        builder.create().show()
    }

    override fun onItemClick(photo: Photo) {
        println(photo.path)
        println(photo.name)
        startActivity(
            Intent(appContext(), ViewerActivity::class.java)
                .putExtra(PHOTO_NAME, photo.name)
                .putExtra(PHOTO_PATH, photo.path)
        )
    }

    override fun onClick(v: View?) {
        //image selection dialog
        selectImage(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

    companion object {
        const val INTENT_EXTRAS: String = "INTENT_EXTRAS"
        const val REQUEST_CODE: String = "REQUEST_CODE"
        const val RESULT_CODE: String = "RESULT_CODE"
        const val PHOTO_PATH: String = "PHOTO_PATH"
        const val PHOTO_NAME: String = "PHOTO_NAME"
    }
}