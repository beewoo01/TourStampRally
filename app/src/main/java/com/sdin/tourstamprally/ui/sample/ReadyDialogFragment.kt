package com.sdin.tourstamprally.ui.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sdin.tourstamprally.databinding.DialogReadyBinding

class ReadyDialogFragment :  DialogFragment(){

    private lateinit var binding : DialogReadyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReadyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: 2021/12/15 Dialg dismiss

    }
}