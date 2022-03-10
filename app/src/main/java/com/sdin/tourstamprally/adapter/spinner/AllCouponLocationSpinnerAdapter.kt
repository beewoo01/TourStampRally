package com.sdin.tourstamprally.adapter.spinner

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sdin.tourstamprally.R

class AllCouponLocationSpinnerAdapter(
    val context: Context,
    val list: MutableList<Triple<Int, String, MutableList<Pair<Int, String>>?>>?
) : BaseAdapter() {


    override fun getCount(): Int {
        return if (list != null) {
            list.size - 1
        } else {
            0
        }
    }

    override fun getItem(position: Int): Any {
        return list?.get(position) ?: 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        if (view == null) {
            view =
                LayoutInflater.from(context)
                    .inflate(R.layout.store_coupon_spinner_layout, parent, false)
        }

        if (list != null) {
            if (position == count) {
                view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                    text = ""
                    hint = list[count].second
                }
            } else {
                val text: String = list[position].second
                view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                    this.text = text
                    parent?.context?.let {
                        this.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }

                }
            }

        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view: View? = convertView
        if (view == null) {
            view =
                LayoutInflater.from(context)
                    .inflate(R.layout.store_coupon_spinner_layout, parent, false)
        }

        if (list != null) {
            if (position == count) {
                view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                    text = ""
                    hint = list[count].second
                }

            } else {
                val text: String = list[position].second
                view?.findViewById<TextView>(R.id.nameTxv)?.apply {
                    this.text = text
                    parent?.context?.let {
                        this.setTextColor(ContextCompat.getColor(it, R.color.black))
                    }

                }
            }

        }
        return view
    }

}