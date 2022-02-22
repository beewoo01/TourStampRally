package com.sdin.tourstamprally.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.FragmentSelectGuidStoreBinding
import net.daum.mf.map.api.MapView

class SelectGuidStoreFragment : Fragment() {

    private var binding: FragmentSelectGuidStoreBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_select_guid_store, container, false)
        val mapView = MapView(requireContext())
        binding?.apply {
            val mapViewContainer = binding?.mapLayout as ViewGroup
            //mapViewContainer.touchDelegate = TouchDelegate(Rect(0,0,mappad.width, 30), mapView)
            mapViewContainer.addView(mapView)
            bottomSheetGroup.bringToFront()
            bottomSheetGroup.invalidate()
            //storePad.elevation = 10.0F
        }
        return binding?.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            val inflater: LayoutInflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        }


        /*binding?.apply {
            //mapLayout.touchDelegate = TouchDelegate(Rect(0,0, mapLayout.width, ))
            storePad.setOnTouchListener { v, event ->

                when(constraintLayout2.currentState){
                    R.id.base_state -> {
                        constraintLayout2.transitionToState(R.id.half_store)
                    }

                    R.id.half_store -> {
                        constraintLayout2.transitionToState(R.id.full_store)
                        Log.wtf("currentState", "half_store")
                    }

                    R.id.full_store -> {
                        Log.wtf("currentState", "full_store")
                    }
                }

                *//*val action = event.action
                //Log.wtf("setOnTouchListener", "setOnTouchListener")
                if (action == MotionEvent.ACTION_UP) {
                    Log.wtf("ACTION_UP", "ACTION_UP")
                }*//*
                true
            }

        }*/
    }
}