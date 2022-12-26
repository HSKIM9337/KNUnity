package com.example.knunity.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.knunity.board.BoardEditActivity
import com.example.knunity.board.BoardModel
import com.example.knunity.databinding.ActivityBoardInsideBinding



class SpinnerClass: Activity(), AdapterView.OnItemSelectedListener {

    private lateinit var numbers:String
    private lateinit var binding:ActivityBoardInsideBinding
    lateinit var datas: BoardModel
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        datas = intent.getSerializableExtra("data") as BoardModel
        when(p2) {

            0-> {

            }
            1-> {
                FBRef.boardRef.child(datas.key).setValue(BoardModel(binding.titlePage.text.toString(),binding.contentPage.text.toString(),binding.timePage.toString()))

                val intent = Intent(this, BoardEditActivity::class.java)
                intent.putExtra("temp_key", datas.key)
                startActivity(intent)

                Toast.makeText(this, "수정 완료", Toast.LENGTH_SHORT).show()
                //finish()


            }
            2-> {

            }



        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

}