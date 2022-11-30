package com.example.knunity.utils

import android.app.Activity
import android.view.View
import android.widget.AdapterView


class SpinnerClass: Activity(), AdapterView.OnItemSelectedListener {

    private lateinit var numbers:String

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        when(p2) {
            0-> {

            }
            1-> {

            }
            2-> {

            }



        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}