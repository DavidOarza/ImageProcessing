package com.university.ip.ui.editor

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.university.ip.R

class FiltersAdapter(context: Context, val itemClickListener: ItemClickListener) :
    RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {
    var selectedPosition = -1;

    //    var selections: MutableList<Photo> = mutableListOf()
    var selectMedia: (filter: String, Int) -> Unit =
        { filter, _ -> print(filter) }
    private val context = context

    private var list: List<String> = arrayListOf()
    private var inflater: LayoutInflater? = LayoutInflater.from(context)

    fun getList(): List<String> {
        return list
    }

    fun setMediaList(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater!!.inflate(R.layout.filter_item, parent, false)
        return ViewHolder(context, view, selectMedia)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(context: Context, view: View, val selectMedia: (String, Int) -> Unit) :
        RecyclerView.ViewHolder(view) {

        fun bindView(item: String) {
            val textView: TextView = itemView.findViewById(R.id.filter_name)
            textView.text = item

            textView.setOnClickListener {
                selectMedia(item, adapterPosition)
                itemClickListener.onItemClick(item)
            }
        }
    }

    interface ItemClickListener {
        fun onItemClick(filter: String)
    }
}