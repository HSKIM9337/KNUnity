package com.example.knunity.utils

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_board_inside.*

class SpinnerClass: Activity(), AdapterView.OnItemSelectedListener {

    private lateinit var numbers:String

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        when(spinner.getItemAtPosition(p2)) {



        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}