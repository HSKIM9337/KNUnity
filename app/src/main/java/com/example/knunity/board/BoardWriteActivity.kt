package com.example.knunity.board

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.knunity.databinding.ActivityBoardWriteBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef

class BoardWriteActivity : AppCompatActivity() {
    private val binding: ActivityBoardWriteBinding by lazy {
        ActivityBoardWriteBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        write()
    }

    private fun write(){
        binding.writebtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val contents = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val key = FBRef.boardRef.push().key.toString()
            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(uid, title, contents, time))
            Toast.makeText(this, "게시글을 썼습니다", Toast.LENGTH_SHORT).show()
        finish()
        }
    }
}