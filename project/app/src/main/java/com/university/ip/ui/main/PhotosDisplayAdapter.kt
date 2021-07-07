package com.university.ip.ui.main

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R
import com.university.ip.model.Photo


class PhotosDisplayAdapter(context: Context, val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<PhotosDisplayAdapter.ViewHolder>() {

    //    var selections: MutableList<Photo> = mutableListOf()
    var selectMedia: (photo: Photo, Int) -> Unit =
        { photo, _ -> print(photo.toString()) }
    private val context = context

    private var list: List<Photo> = arrayListOf()
    private var inflater: LayoutInflater? = LayoutInflater.from(context)

    //getter and setter for the list
    fun getList(): List<Photo> {
        return list
    }

    fun setMediaList(list: List<Photo>) {
        this.list = list
        notifyDataSetChanged()
    }

    //this is where create the inflater used to put items inside recycler
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater!!.inflate(R.layout.photos_display_item, parent, false)
        return ViewHolder(context, view, selectMedia)
    }

    //bind of the element with view item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(context: Context, view: View, val selectMedia: (Photo, Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        //this function should be changed when the view objects are changed
        fun bindView(item: Photo) {
            val imageView: ImageView = itemView.findViewById(R.id.image)
            val textView: TextView = itemView.findViewById(R.id.name)
            val bitmap = BitmapFactory.decodeFile(item.path)
            imageView.setImageBitmap(bitmap)
            textView.text = item.name

            //click listener that will be implemented in view
            imageView.setOnClickListener {
                selectMedia(item, adapterPosition)
                itemClickListener.onItemClick(item)
            }
        }
    }

    //click listener interface
    interface ItemClickListener {
        fun onItemClick(photo: Photo)
    }
}