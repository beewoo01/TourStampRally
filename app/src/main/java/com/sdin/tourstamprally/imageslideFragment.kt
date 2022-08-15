package com.sdin.tourstamprally

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sdin.tourstamprally.databinding.FragmentImageslideBinding


class imageslideFragment(val image : Int) : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewBind: FragmentImageslideBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBind = DataBindingUtil.inflate(inflater, R.layout.fragment_imageslide, container, false)
        viewBind.imgview.setImageResource(image)
        return viewBind.root
    }

}