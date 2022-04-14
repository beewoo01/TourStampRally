package com.sdin.tourstamprally.ui.dialog.reviewcommnet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sdin.tourstamprally.R

class ReportDialog(val callback : (String) -> Unit) : BottomSheetDialogFragment(){


    private lateinit var radioGroup : RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_report, container, false)
        radioGroup = view.findViewById(R.id.radio_group)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        view.findViewById<Button>(R.id.report_btn).setOnClickListener {
            val id = radioGroup.checkedRadioButtonId
            if (id > -1) {
                val selected : RadioButton = view.findViewById(id)
                callback(selected.text.toString())
                dismiss()
            } else {
                Toast.makeText(requireContext(), "신고샤유를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }

        }

        view.findViewById<ImageButton>(R.id.close_imb).setOnClickListener {
            dismiss()
        }

    }

}