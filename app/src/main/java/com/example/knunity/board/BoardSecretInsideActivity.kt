package com.example.knunity.board

import android.R
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.knunity.comment.CommentAdapter
import com.example.knunity.comment.CommentBoardModel
import com.example.knunity.comment.CommentModel
import com.example.knunity.comment.MycommentModel
import com.example.knunity.databinding.ActivityBoardInsideBinding
import com.example.knunity.databinding.ActivityBoardSecretInsideBinding
import com.example.knunity.secret.SecretBoardModel
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.FBRef.Companion.reportRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage

class BoardSecretInsideActivity : AppCompatActivity() {
    private lateinit var key: String

    private val binding: ActivityBoardSecretInsideBinding by lazy {
        ActivityBoardSecretInsideBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter2: CommentAdapter by lazy {
        CommentAdapter()
    }
    lateinit var datas: SecretBoardModel
    private val commentDataList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()
    private val likeList = mutableListOf<String>()
    private val alllikeList = mutableListOf<String>()
    private val scrapList = mutableListOf<String>()
    private val allscrapList = mutableListOf<String>()
    private val commentCountList = mutableListOf<String>()
    private var newCountList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        datas = intent.getSerializableExtra("data") as SecretBoardModel
        binding.titlePage.text = datas.title
        binding.contentPage.text = datas.contents
        binding.timePage.text = datas.time
        val temp_keys = datas.key
        key = temp_keys
        val writeuid = datas.userUid

        // commentCheck(temp_keys)
        val youuid = FBAuth.getUid()
        val spinner1 = arrayListOf<String>("▼","신고", "수정", "삭제")
        //  val spinner1 = arrayListOf<String>("신고", "수정", "삭제")
        val spinner2 = arrayListOf<String>("▼","신고")
        //val spinner2 = arrayListOf<String>("신고")
        val spinner1_Parent =
            ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, spinner1)
        val spinner2_Parent =
            ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, spinner2)
        if (writeuid.equals(youuid)) {
            binding.spinner.adapter = spinner1_Parent

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {
                        }
                    if(p2==1)
                    {
                        val builder = AlertDialog.Builder(this@BoardSecretInsideActivity)
                        builder.setTitle("신고합니다.")
                            .setMessage("신고하시겠습니까?")
                            .setNegativeButton("확인") { dialog, _ ->
                                // 현재 유저의 ID 가져오기
                                val userId = FBAuth.getUid()
                                val postId = temp_keys
                                // 해당 게시물에 대한 신고 정보 가져오기
                                reportRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(userId)) {
                                            // 이미 신고한 유저인 경우
                                            Toast.makeText(this@BoardSecretInsideActivity, "이미 신고하셨습니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // 처음 신고하는 유저인 경우
                                            val reportId = reportRef.child(postId).push().key
                                            val reportData = hashMapOf(
                                                "userId" to userId,
                                                "timestamp" to ServerValue.TIMESTAMP
                                            )
                                            reportRef.child(postId).child(userId).setValue(reportData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@BoardSecretInsideActivity, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(this@BoardSecretInsideActivity, "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(this@BoardSecretInsideActivity, "오류가 발생했습니다: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }

                                })

                                // 삭제 여부 확인하기
                                reportRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.childrenCount >= 2) {
                                            // 두 명 이상이 신고한 경우
                                            // 게시물 삭제 처리하기
                                            FBRef.secretboardRef.child(postId).removeValue()
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@BoardSecretInsideActivity, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                                    finish()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(this@BoardSecretInsideActivity, "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            }
                            .setPositiveButton("취소")
                            {dialog,_->
                                dialog.dismiss()
                            }
                        val alertDialog = builder.create()
                        alertDialog.show()
         }
                    if (p2 == 2) {
                        editPage(temp_keys)
                    }
                    if (p2 == 3) {
                        try {
                            FBRef.likeboardRef.child(temp_keys).removeValue()
                            FBRef.secretboardRef.child(temp_keys).removeValue()
                            FBRef.scrapboardRef.child(temp_keys).removeValue()
                            FBRef.comboardRef.child(temp_keys).removeValue()
                            getRemoveData(temp_keys)
                            finish()
                        } catch (e: StorageException) {
                            Toast.makeText(parent, "오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        } else {
            binding.spinner.adapter = spinner2_Parent
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {
                        //editPage(temp_keys)
                    }
                    if (p2 == 1) {
                        val builder = AlertDialog.Builder(this@BoardSecretInsideActivity)
                        builder.setTitle("신고합니다.")
                            .setMessage("신고하시겠습니까?")
                            .setNegativeButton("확인") { dialog, _ ->
                                // 현재 유저의 ID 가져오기
                                val userId = FBAuth.getUid()
                                val postId = temp_keys
                                // 해당 게시물에 대한 신고 정보 가져오기
                                reportRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(userId)) {
                                            // 이미 신고한 유저인 경우
                                            Toast.makeText(this@BoardSecretInsideActivity, "이미 신고하셨습니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // 처음 신고하는 유저인 경우
                                            val reportData = hashMapOf(
                                                "userId" to userId,
                                                "timestamp" to ServerValue.TIMESTAMP
                                            )
                                            reportRef.child(postId).child(userId).setValue(reportData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@BoardSecretInsideActivity, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(this@BoardSecretInsideActivity, "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(this@BoardSecretInsideActivity, "오류가 발생했습니다: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }

                                })

                                // 삭제 여부 확인하기
                                reportRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.childrenCount >= 2) {
                                            // 두 명 이상이 신고한 경우
                                            // 게시물 삭제 처리하기
                                            FBRef.secretboardRef.child(postId).removeValue()
                                                .addOnSuccessListener {
                                                    Toast.makeText(this@BoardSecretInsideActivity, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                                                    finish()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(this@BoardSecretInsideActivity, "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }

                                        }

                                    }

                                    override fun onCancelled(error: DatabaseError) {}
                                })
                            }
                            .setPositiveButton("취소")
                            {dialog,_->
                                dialog.dismiss()
                            }.show()

                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
        //commentCheck(temp_keys)
        //Log.d("cocheck",commentCountList.toString())
        likeCheck(temp_keys)
        scrapCheck(temp_keys)
        // commentCheck(temp_keys)
        binding.likeBtn.setOnClickListener {
            binding.likeBtn.isSelected = !binding.likeBtn.isSelected
            if (likeList.contains(FBAuth.getUid())) {
                dislike(temp_keys)
            } else {
                likethis(temp_keys)
            }
        }
        binding.scrapBtn.setOnClickListener {
            binding.scrapBtn.isSelected = !binding.scrapBtn.isSelected
            if (scrapList.contains(FBAuth.getUid())) {
                unscrap(temp_keys)
            } else {
                scrap(temp_keys)
            }
        }

        Log.d("test", temp_keys)

        binding.commentBtn.setOnClickListener {
            commentCheck(temp_keys)
            comment(temp_keys)
            commentboard(temp_keys)
        }
        getCommentData(temp_keys)
        onBackPressed()
        useRV2()
        getImagefromFB(temp_keys + ".png")
    }

    private fun getCommentData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentDataList.clear()
                commentCountList.clear()
                for (dataModel in snapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    Log.d("comment2", item.toString())
                    if (key.equals(item?.boardKeyda)) {
                        commentDataList.add(item!!)
                        commentKeyList.add(dataModel.key.toString()) //키값을 전달받는다.
                    }
                }
                binding.commentCount.text = commentDataList.size.toString()
                myRecyclerViewAdapter2.submitList(commentDataList.toList())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.commentRef.addValueEventListener(postListener)
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

    private fun commentCheck(key: String)
    {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentDataList.clear()
                //newCountList.clear()
                for (dataModel in snapshot.child(key).children) {
                    val item = dataModel.getValue(MycommentModel::class.java)
                    val uuuid =dataModel.key.toString()
                    Log.d("comment3", dataModel.toString())
                    Log.d("comment4",dataModel.key.toString())
                    commentCountList.add(uuuid)
                }
                Log.d("cocount44", commentCountList.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.mycommentRef.addValueEventListener(postListener)
    }
    private fun commentCount(key: String)
    {
        val commentTitle = binding.commentArea.text.toString()
        val commentCreatedTime = FBAuth.getTime()
        val commenteryUid = FBAuth.getUid()
        Log.d("co_list",commentCountList.toString())
        //    Log.d("colist2",colist.toString())
        val mymykey = FBRef.mycommentRef.push().key.toString()
//        val coUid = "익명"+commentCountList.indexOf(FBAuth.getUid())
//        val mykey = FBRef.commentRef.push().key.toString()
        FBRef.mycommentRef
            .child(key)
            .child(FBAuth.getUid()).child(mymykey)
            .setValue(MycommentModel(key,datas.userUid, datas.title, datas.contents, datas.time))
    }

    private fun comment(key: String) {
        val commentTitle = binding.commentArea.text.toString()
        val commentCreatedTime = FBAuth.getTime()
        commentCountList.distinct()
        commentCountList.reversed()
        val mymykey = FBRef.mycommentRef.push().key.toString()
        val mykey = FBRef.commentRef.push().key.toString()
        if(FBAuth.getUid().equals(datas.userUid)){
//            val subuid = FBAuth.getUid().substring(FBAuth.getUid().length-6,FBAuth.getUid().length)
            val coUid = "익명(작성자)"
//            val mykey = FBRef.commentRef.push().key.toString()
            Log.d("colist2",commentCountList.toString())
            FBRef.commentRef
                .child(mykey)
                .setValue(CommentModel(commentTitle, commentCreatedTime, coUid, key))
            Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        }
        else
        {
        val subuid = FBAuth.getUid().substring(FBAuth.getUid().length-6,FBAuth.getUid().length)
        val coUid = "익명("+subuid+")"
   //            val mykey = FBRef.commentRef.push().key.toString()
        Log.d("colist2",commentCountList.toString())
        FBRef.commentRef
            .child(mykey)
            .setValue(CommentModel(commentTitle, commentCreatedTime, coUid, key))
        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        }
        FBRef.mycommentRef
            .child(key)
            .child(FBAuth.getUid()).child(mymykey)
            .setValue(MycommentModel(key,datas.userUid, datas.title, datas.contents, datas.time))
        binding.commentArea.setText("")
    }

    private fun commentboard(key: String)
    {
        val title = binding.titlePage.text.toString()
        val contents = binding.contentPage.text.toString()
        val time = binding.timePage.text.toString()
        FBRef.comboardRef.child(key).child(FBAuth.getUid())
            // .setValue(ScrapModel(FBAuth.getUid()))
            .setValue(CommentBoardModel(key, FBAuth.getUid(), title, contents, time))
    }

    private fun useRV2() {
        binding.rvList2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = myRecyclerViewAdapter2
        }
    }

    private fun editPage(key: String) {
        val intent = Intent(this, BoardEditActivity::class.java)
        intent.putExtra("temp_key", datas.key)
        startActivity(intent)
        finish()
    }

    private fun getImagefromFB(key: String) {
        val storageReference = Firebase.storage.reference.child(key)
        val imageViewFromFB = binding.imagePage
        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->

            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                binding.imagePage.isVisible = false
                imageViewFromFB.isVisible = false
                Toast.makeText(this, key, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        binding.writeBack.setOnClickListener {
            //startActivity(Intent(this, BoardListActivity::class.java))
            finish()
        }
    }

    private fun likeCheck(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likeList.clear()
                alllikeList.clear()
                for (dataModel in snapshot.child(key).children) {
                    val item = dataModel.getValue(LikingBoardModel::class.java)
                    Log.d("item", item?.userUid.toString())
                    alllikeList.add(item!!.toString())
                    if (FBAuth.getUid().equals(item?.userUid)) {
                        likeList.add((item?.userUid.toString()))
                    } else {
                    }
                }
                Log.d("hh2", likeList.toString())
                binding.likeCount.text = alllikeList.size.toString()
                if (likeList.contains(FBAuth.getUid())) {
                    binding.likeBtn.isSelected = true
                } else {
                    binding.likeBtn.isSelected = false
                }
                if(alllikeList.size>=2)
                {
                    FBRef.likeboardRef.child(key).setValue(LikeBoardModel("비밀게시판",key, FBAuth.getUid(), datas.title, datas.contents, datas.time))
                }
                else
                {
                    FBRef.likeboardRef.child(key).removeValue()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }

        FBRef.likeRef.addValueEventListener(postListener)
    }

    private fun likethis(key: String) {
        val title = binding.titlePage.text.toString()
        val contents = binding.contentPage.text.toString()
        val time = binding.timePage.text.toString()
        FBRef.likeRef.child(key).child(FBAuth.getUid()).setValue(LikingBoardModel(FBAuth.getUid()))

        Log.d("like", likeList.size.toString())
    }

    private fun dislike(key: String) {
        FBRef.likeRef.child(key).child(FBAuth.getUid()).removeValue()
    }

    private fun scrapCheck(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                scrapList.clear()
                allscrapList.clear()
                for (dataModel in snapshot.child(key).children) {
                    val item = dataModel.getValue(ScrapModel::class.java)
                    Log.d("scrap", item?.userUid.toString())
                    allscrapList.add(item!!.toString())
                    if (FBAuth.getUid().equals(item?.userUid)) {
                        scrapList.add((item?.userUid.toString()))
                    }
                }
                binding.scrapCount.text = allscrapList.size.toString()
                if (scrapList.contains(FBAuth.getUid())) {
                    binding.scrapBtn.isSelected = true
                } else {
                    binding.scrapBtn.isSelected = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.scrapboardRef.addValueEventListener(postListener)
    }

    private fun scrap(key: String)
    {
        val title = binding.titlePage.text.toString()
        val contents = binding.contentPage.text.toString()
        val time = binding.timePage.text.toString()
        FBRef.scrapboardRef.child(key).child(FBAuth.getUid())
            // .setValue(ScrapModel(FBAuth.getUid()))
            .setValue(ScrapModel("비밀게시판",key, FBAuth.getUid(), title, contents, time))
    }

    private fun unscrap(key: String) {
        FBRef.scrapboardRef.child(key).child(FBAuth.getUid()).removeValue()
    }

}