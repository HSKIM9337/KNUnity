package com.example.knunity.board


import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.knunity.comment.CommentAdapter
import com.example.knunity.comment.CommentBoardModel
import com.example.knunity.comment.CommentModel
import com.example.knunity.comment.MycommentModel
import com.example.knunity.databinding.ActivityBoardInsideBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage


class BoardInsideActivity : AppCompatActivity() {
    private lateinit var key: String
    private lateinit var nicknametotal : String
    private val binding: ActivityBoardInsideBinding by lazy {
        ActivityBoardInsideBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter2: CommentAdapter by lazy {
        CommentAdapter()
    }
    lateinit var datas: BoardModel
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

        datas = intent.getSerializableExtra("data") as BoardModel
        binding.whatboard.text=datas.what
        binding.titlePage.text = datas.title
        binding.contentPage.text = datas.contents
        binding.timePage.text = datas.time
        binding.nick.text=datas.nick
        val temp_keys = datas.key
        key = temp_keys
        FBRef.userRef.child(FBAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModel = snapshot.getValue(UserModel::class.java)
                nicknametotal = userModel?.nickname.toString() // 가져온 닉네임 정보

                // 가져온 닉네임 정보를 사용하여 필요한 작업 수행
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 발생시 처리
                Log.w("FirebaseTest", "Failed to read value.", error.toException())
            }
        })
        var currentDialog: AlertDialog? = null
        val writeuid = datas.uid
       // commentCheck(temp_keys)
        val youuid = FBAuth.getUid()
        val spinner1 = arrayListOf<String>("▼","신고", "수정", "삭제")
      //  val spinner1 = arrayListOf<String>("신고", "수정", "삭제")
        val spinner2 = arrayListOf<String>("▼","신고")
        //val spinner2 = arrayListOf<String>("신고")
        val spinner1_Parent =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinner1)
        val spinner2_Parent =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinner2)
        if (writeuid.equals(youuid)) {
            binding.spinner.adapter = spinner1_Parent

            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {
//                        val intent = Intent(this@BoardInsideActivity, BoardDeclarationActivity::class.java)
//                        startActivity(intent)
                    }
                    if(p2==1)
                    {
                        val intent = Intent(this@BoardInsideActivity, BoardDeclarationActivity::class.java)
                        intent.putExtra("postId", datas.uid)
                        intent.putExtra("whatb",binding.whatboard.text)
                        intent.putExtra("thiskey",key)
                       startActivity(intent)
                    }
                    if (p2 == 2) {
                        editPage(temp_keys)
                    }
                    if ((p2 == 3)&&(datas.what=="자유게시판")) {
                        try {
                            FBRef.likeboardRef.child(temp_keys).removeValue()
                            FBRef.boardRef.child(temp_keys).removeValue()
                            FBRef.scrapboardRef.child(temp_keys).removeValue()
                            FBRef.comboardRef.child(temp_keys).removeValue()
                            getRemoveData(temp_keys)
                            finish()
                        } catch (e: StorageException) {
                            Toast.makeText(parent, "오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if((p2 == 3)&&(datas.what=="취업게시판"))
                    {
                        try {
                            FBRef.likeboardRef.child(temp_keys).removeValue()
                            FBRef.jobRef.child(temp_keys).removeValue()
                            FBRef.scrapboardRef.child(temp_keys).removeValue()
                            FBRef.comboardRef.child(temp_keys).removeValue()
                            getRemoveData(temp_keys)
                            finish()
                        } catch (e: StorageException) {
                            Toast.makeText(parent, "오류", Toast.LENGTH_SHORT).show()
                        }
                    }
                    if((p2 == 3)&&(datas.what=="구인/구직게시판"))
                    {
                        try {
                            FBRef.likeboardRef.child(temp_keys).removeValue()
                            FBRef.jobRef.child(temp_keys).removeValue()
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
                        val builder = AlertDialog.Builder(this@BoardInsideActivity)
                        builder.setTitle("신고합니다.")
                            .setMessage("신고하시겠습니까?")
                            .setNegativeButton("확인") { dialog, _ ->
                                // 현재 유저의 ID 가져오기
                                val userId = FBAuth.getUid()
                                val postId = temp_keys
                                // 해당 게시물에 대한 신고 정보 가져오기
                                FBRef.reportRef.child(postId).addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(userId)) {
                                            // 이미 신고한 유저인 경우
                                            Toast.makeText(this@BoardInsideActivity, "이미 신고하셨습니다.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // 처음 신고하는 유저인 경우
                                            val reportId = FBRef.reportRef.child(postId).push().key
                                            val reportData = hashMapOf(
                                                "userId" to userId,
                                                "timestamp" to ServerValue.TIMESTAMP
                                            )
                                            FBRef.reportRef.child(postId).child(userId).setValue(reportData)
                                                .addOnSuccessListener {
                                                    // Toast.makeText(this@BoardSecretInsideActivity,snapshot.childrenCount.toString(),Toast.LENGTH_SHORT).show()
                                                    Toast.makeText(this@BoardInsideActivity, "신고가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(this@BoardInsideActivity, "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(this@BoardInsideActivity, "오류가 발생했습니다: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }

                                })
                                // 삭제 여부 확인하기
                                dialog.dismiss()
                                currentDialog = null
                            }
                            .setPositiveButton("취소")
                            {dialog,_->
                                dialog.dismiss()
                                currentDialog = null
                            }
                        val alertDialog = builder.create()
                        alertDialog.show()
                        currentDialog = alertDialog
                        binding.spinner.setSelection(0)
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
        FBRef.reportRef.child(temp_keys).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.childrenCount >= 2) {
                    // 두 명 이상이 신고한 경우
                    // 게시물 삭제 처리하기
                    FBRef.reportRef.child(temp_keys).removeEventListener(this) // 이벤트 리스너 등록 해제
                    FBRef.secretboardRef.child(temp_keys).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(this@BoardInsideActivity, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@BoardInsideActivity, "오류가 발생했습니다: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
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
        //getContentFromFB(temp_keys)
        getImagefromFB(temp_keys)
    }

    private fun getImagefromFB(key: String) {
        val storageReference = Firebase.storage.reference
        val imageViewFromFB = binding.imagePage
        val videoViewFromFB = binding.videoView
        val gifViewFromFB = binding.webView
        storageReference.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->

                if (item.name.substring(0, item.name.length - 4) == key) {
                    // key와 일치하는 파일을 찾음
                    val extension = item.name.takeLast(4).lowercase() // 파일 이름에서 마지막 4글자 추출
                    Log.d("extension", extension)

                    if (extension == ".png") {
                        // 이미지 처리 로직
                        imageViewFromFB.isVisible=true
                        videoViewFromFB.isVisible=false
                        gifViewFromFB.isVisible=false
                        storageReference.child(key+".png").downloadUrl.addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                Log.d("check",task.isSuccessful.toString())
                                Glide.with(this)
                                    .load(task.result)
                                    .into(imageViewFromFB)
                            } else {
                                imageViewFromFB.isVisible = true
                                videoViewFromFB.isVisible = false
                                gifViewFromFB.isVisible = false
                                Toast.makeText(this, key, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else if (extension == ".mp4") {
                        // 비디오 처리 로직
                        videoViewFromFB.isVisible = true
                        imageViewFromFB.isVisible = false
                        gifViewFromFB.isVisible = false
                        storageReference.child(key+".mp4").downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                videoViewFromFB.setVideoURI(task.result)
                                videoViewFromFB.start()
                            } else {
                                imageViewFromFB.isVisible = false
                                videoViewFromFB.isVisible = true
                                gifViewFromFB.isVisible = false
                                Toast.makeText(this, "Failed to load content", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else if (extension == ".gif") {
                        // 움짤 처리 로직
                        gifViewFromFB.isVisible = true
                        imageViewFromFB.isVisible = false
                        videoViewFromFB.isVisible = false
                        gifViewFromFB.loadUrl(storageReference.toString())
                    }
                }
            }
        }.addOnFailureListener { exception ->
            // 실패 처리 로직
        }

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

    private fun comment(key: String) {
            val commentTitle = binding.commentArea.text.toString()
            val commentCreatedTime = FBAuth.getTime()
        commentCountList.distinct()
        commentCountList.reversed()
        val mymykey = FBRef.mycommentRef.push().key.toString()
        val coUid = "익명"+commentCountList.indexOf(FBAuth.getUid())
        val mykey = FBRef.commentRef.push().key.toString()
        Log.d("colist2",commentCountList.toString())

        FBRef.commentRef
            .child(mykey)
            .setValue(CommentModel(commentTitle, commentCreatedTime, nicknametotal, key,mykey,FBAuth.getUid()))
        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
            FBRef.mycommentRef
                .child(key)
                .child(FBAuth.getUid()).child(mymykey)
                .setValue(MycommentModel(key,datas.uid, datas.title, datas.contents, datas.time))
            binding.commentArea.setText("")
    }

    private fun commentboard(key: String)
    {
        val title = binding.titlePage.text.toString()
        val contents = binding.contentPage.text.toString()
        val time = binding.timePage.text.toString()
        FBRef.comboardRef.child(key).child(FBAuth.getUid())
            // .setValue(ScrapModel(FBAuth.getUid()))
            .setValue(CommentBoardModel("자유게시판",key, FBAuth.getUid(), title, contents, time,datas.nick))
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
                    FBRef.likeboardRef.child(key).setValue(LikeBoardModel("자유게시판",key, FBAuth.getUid(), datas.title, datas.contents, datas.time,datas.nick))
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
            .setValue(ScrapModel("자유게시판",key, FBAuth.getUid(), title, contents, time,datas.nick))
    }

    private fun unscrap(key: String) {
        FBRef.scrapboardRef.child(key).child(FBAuth.getUid()).removeValue()
    }

}
//게시판 이름 맞춰서 데이터베이스 저장
//신고 게시판 완성
//프레그먼트 즐겨찾기 완성
//나머지 게시판 완성(비밀게시판은 익명,나머지는 공개)