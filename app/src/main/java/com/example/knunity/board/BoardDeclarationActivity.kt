package com.example.knunity.board

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.knunity.databinding.ActivityBoardDeclarationBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardDeclarationActivity : AppCompatActivity() {
    private val binding: ActivityBoardDeclarationBinding by lazy {
        ActivityBoardDeclarationBinding.inflate(layoutInflater)
    }
    lateinit var datas: DeclarationBoardModel
    private val keylist= mutableListOf<String>()
    private val submitlist = mutableListOf<String>()
    private val allsubmitlist = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        datas = intent.getSerializableExtra("data") as DeclarationBoardModel
//        val temp_key = datas.key
//        Log.d("this key",temp_key)
        submitCheck(FBAuth.getUid())
        binding.submitbtn.setOnClickListener {
            submit(FBAuth.getUid())
        }
            onBackPressed()
    }
    private fun submit(key:String)
    {
        FBRef.declatioinRef.child(key).child(FBAuth.getUid()).setValue(BoardDeclarationModel(FBAuth.getUid(),key))
        Toast.makeText(parent, "제출완료", Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        binding.cancelbtn.setOnClickListener {
            finish()
        }
    }
    private fun submitCheck(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                keylist.clear()
                allsubmitlist.clear()
                for (dataModel in snapshot.child(key).children) {

                    val item = dataModel.getValue(BoardDeclarationModel::class.java)
                    allsubmitlist.add(item!!.toString())
                    if (FBAuth.getUid().equals(item?.userUid)) {
                        submitlist.add((item?.userUid.toString()))
                        Toast.makeText(parent,"제출완료",Toast.LENGTH_SHORT).show()
                    }
                    else {
                    }
                }
               // Log.d("hh2", likeList.toString())
                //binding.likeCount.text = alllikeList.size.toString()
                if (submitlist.contains(FBAuth.getUid())) {
                   // submitlist.add()
                } else {
                    //binding.likeBtn.isSelected = false
                }
                if(allsubmitlist.size>=2)
                {
                    FBRef.boardRef.child(key).removeValue()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.likeRef.addValueEventListener(postListener)
    }
}