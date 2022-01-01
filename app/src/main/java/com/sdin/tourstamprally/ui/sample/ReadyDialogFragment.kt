package com.sdin.tourstamprally.ui.sample

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.DialogReadyBinding

class ReadyDialogFragment() : DialogFragment() {

    private lateinit var binding: DialogReadyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DialogReadyBinding.inflate(inflater, container, false)
        binding.cancleBtn.setOnClickListener {
            dismiss()
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            //setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.boldColor)))
            clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }


    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_ready_fragment,
            null,
            false
        )
        setContentView(binding.root)

        window?.apply {
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes.windowAnimations = R.style.AnimationPopupStyle
        }


    }*/
}