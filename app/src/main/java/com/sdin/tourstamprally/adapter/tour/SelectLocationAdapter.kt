package com.sdin.tourstamprally.adapter.tour

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.sdin.tourstamprally.R

class SelectLocationAdapter(val data : MutableList<String>, val context : Context) : BaseAdapter(){

    private val inflater : LayoutInflater

    init {
        clearItem()
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_title_item, parent, false)

            if (data != null) {
                val text = data[position]
                val textView = convertView?.findViewById<TextView>(R.id.spinnerTitleText)
                textView?.text = text

                val imageButton = convertView?.findViewById<ImageButton>(R.id.imagebtn)
                imageButton?.isEnabled = false
            }
        }
        return view!!
    }

    private fun clearItem() {
        if (data != null) {
            data.clear()
            notifyDataSetChanged()
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false)
        }

        view?.findViewById<TextView>(R.id.spinnerText)?.text = data[position]

        return view!!
    }
}