package com.example.knunity.comment

import android.R
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.knunity.board.BoardInsideActivity
import com.example.knunity.comment.CommentModel
import com.example.knunity.databinding.CommentListItemBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class CommentHolder(private val binding: CommentListItemBinding)
    :RecyclerView.ViewHolder(binding.root)
{
    private val spinnerItems = arrayListOf<String>("▼","삭제")
    val spinner : Spinner=binding.spinner
    private var currentKey: String? = null
    private var currentUid: String? = null
    private var uid: String? = null
    private var boardKey: String? = null
    fun bind(data:CommentModel) {
        with(binding) {
            titleCo.text = data.commentTitle
            timeCo.text=data.commentCreatedTime
            uidCo.text=data.commenteryUid

            currentKey = data.currentkey
            currentUid=data.uid
            boardKey=data.boardKeyda

        }
       // Log.d("ch3",currentUid.toString())
        if (currentUid.toString().equals(FBAuth.getUid())) {
            spinner.visibility = View.VISIBLE
        } else {
            spinner.visibility = View.GONE
        }

    }
    init {
        val adapter = ArrayAdapter<String>(
            binding.root.context,
            R.layout.simple_spinner_item,
            spinnerItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Spinner에서 선택된 항목에 대한 처리 구현

                if(position==1)
                {
                    Log.d("ch2",currentKey.toString())
                    // 삭제 버튼 눌렀을 때의 처리
                    // temp_keys 대신 comment.key를 사용
                    currentKey?.let { key ->
                        FBRef.commentRef.child(key).removeValue()
                    FBRef.comboardRef.child(boardKey.toString()).child(FBAuth.getUid()).removeValue()
                        Toast.makeText(binding.root.context,"댓글이 삭제되었습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Spinner에서 선택된 항목이 없을 경우의 처리 구현
            }
        }
    }
}





