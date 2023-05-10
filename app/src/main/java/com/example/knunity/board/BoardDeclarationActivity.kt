package com.example.knunity.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.knunity.comment.CommentModel
import com.example.knunity.databinding.ActivityBoardDeclarationBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class BoardDeclarationActivity : AppCompatActivity() {
    private val binding: ActivityBoardDeclarationBinding by lazy {
        ActivityBoardDeclarationBinding.inflate(layoutInflater)
    }
    private lateinit var postId: String
    private lateinit var temp_keys: String
    private lateinit var currentUserId: String
    private lateinit var whatBoard: String
    lateinit var datas: DeclarationBoardModel
    private val keylist = mutableListOf<String>()
    private val submitlist = mutableListOf<String>()
    private val allsubmitlist = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        postId = intent.getStringExtra("postId") ?: ""
        currentUserId = FBAuth.getUid()
        temp_keys = intent.getStringExtra("thiskey") ?: ""
        whatBoard = intent.getStringExtra("whatb") ?: ""
        binding.submitbtn.setOnClickListener {
            checkReport()
            Log.d("currentuid",currentUserId)
            Log.d("post",postId)
            Log.d("whatboard",whatBoard)
            Log.d("tempkey",temp_keys)
        }
    }

    private fun checkReport() {
        val database = Firebase.database.reference

        // user_reports 노드에서 해당 글을 신고한 사용자들의 id를 가져옴
       FBRef.userreportRef.child(postId).get().addOnSuccessListener { dataSnapshot ->
            val reports = dataSnapshot.value as? HashMap<String, Boolean>

            // 이미 신고한 사용자인 경우
            if (reports != null && reports.containsKey(currentUserId)) {
                Toast.makeText(this, "이미 신고한 글입니다.", Toast.LENGTH_SHORT).show()
            } else {
                // 신고하지 않은 사용자인 경우, 글을 신고하고 신고 횟수를 확인함
                reportPost()
            }
        }
    }

    private fun reportPost() {
        FBRef.userreportRef.child(postId).get().addOnSuccessListener { dataSnapshot ->
            val reports = dataSnapshot.value as? HashMap<String, Boolean>

            // 이미 신고한 사용자인 경우
            if (reports != null && reports.containsKey(currentUserId)) {
                Toast.makeText(this, "이미 신고한 글입니다.", Toast.LENGTH_SHORT).show()
            } else {
                FBRef.reportRef.child(temp_keys).child(postId).get()
                    .addOnSuccessListener { dataSnapshot ->
                        val reportCount = dataSnapshot.getValue(Int::class.java)

                        // 신고 횟수가 null인 경우, 0으로 초기화
                        val count = reportCount ?: 0

                        // 신고 횟수를 1 증가시키고, reports 노드에 업데이트함
                        val updatedCount = count + 1
                        FBRef.reportRef.child(temp_keys).child(postId).setValue(updatedCount)
                        // user_reports 노드에 해당 사용자가 해당 글을 신고했음을 저장함
                        FBRef.userreportRef.child(temp_keys).child(postId).child(currentUserId)
                            .setValue(true)
                        // 신고 횟수가 2 이상인 경우, 해당 글을 삭제함
                        if (updatedCount >= 2 && whatBoard == "자유게시판") {
                            //database.child("posts").child(temp_keys).child(postId).removeValue()
                            FBRef.likeboardRef.child(temp_keys).child(postId).removeValue()
                            FBRef.boardRef.child(temp_keys).child(postId).removeValue()
                            FBRef.scrapboardRef.child(temp_keys).child(postId).removeValue()
                            FBRef.comboardRef.child(temp_keys).child(postId).removeValue()
                            FBRef.userreportRef.child(temp_keys).child(postId).removeValue()
                            FBRef.reportRef.child(temp_keys).child(postId).removeValue()
                            getRemoveData(temp_keys)
                            Toast.makeText(this, "신고된 글이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, BoardListActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, BoardListActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
            }
        }
    }
    private fun getRemoveData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataModel in snapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    if (key.equals(item?.boardKeyda)) {
                        FBRef.commentRef.child(dataModel.key.toString()).removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.commentRef.addValueEventListener(postListener)
    }
}

//        datas = intent.getSerializableExtra("data") as DeclarationBoardModel
//        val temp_key = datas.key
//        Log.d("this key",temp_key)
//        submitCheck(FBAuth.getUid())
//        binding.submitbtn.setOnClickListener {
//            submit(FBAuth.getUid())
//        }
//            onBackPressed()
//    }
//    private fun submit(key:String)
//    {
//        FBRef.declatioinRef.child(key).child(FBAuth.getUid()).setValue(BoardDeclarationModel(FBAuth.getUid(),key))
//        Toast.makeText(parent, "제출완료", Toast.LENGTH_SHORT).show()
//    }
//    override fun onBackPressed() {
//        binding.cancelbtn.setOnClickListener {
//            finish()
//        }
//    }
//    private fun submitCheck(key: String) {
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                keylist.clear()
//                allsubmitlist.clear()
//                for (dataModel in snapshot.child(key).children) {
//
//                    val item = dataModel.getValue(BoardDeclarationModel::class.java)
//                    allsubmitlist.add(item!!.toString())
//                    if (FBAuth.getUid().equals(item?.userUid)) {
//                        submitlist.add((item?.userUid.toString()))
//                        Toast.makeText(parent,"제출완료",Toast.LENGTH_SHORT).show()
//                    }
//                    else {
//                    }
//                }
//               // Log.d("hh2", likeList.toString())
//                //binding.likeCount.text = alllikeList.size.toString()
//                if (submitlist.contains(FBAuth.getUid())) {
//                   // submitlist.add()
//                } else {
//                    //binding.likeBtn.isSelected = false
//                }
//                if(allsubmitlist.size>=2)
//                {
//                    FBRef.boardRef.child(key).removeValue()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("check", "loadPost:onCancelled", error.toException())
//            }
//        }
//        FBRef.likeRef.addValueEventListener(postListener)
//    }
//}