package com.example.knunity.board

import android.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.knunity.comment.CommentAdapter
import com.example.knunity.comment.CommentModel
import com.example.knunity.comment.MycommentModel
import com.example.knunity.databinding.ActivityHotBoardInsideBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage

class HotBoardInsideActivity : AppCompatActivity() {
    private lateinit var key: String

    private val binding: ActivityHotBoardInsideBinding by lazy {
        ActivityHotBoardInsideBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter2: CommentAdapter by lazy {
        CommentAdapter()
    }
    lateinit var datas2: LikeBoardModel
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
        datas2 = intent.getSerializableExtra("data") as LikeBoardModel
        binding.whatboard.text=datas2.what
        binding.titlePage.text = datas2.title
        binding.contentPage.text = datas2.contents
        binding.timePage.text = datas2.time
        val temp_keys = datas2.key
        //key = temp_keys
        val writeuid = datas2.userUid
        // commentCheck(temp_keys)
        val youuid = FBAuth.getUid()
        val spinner1 = arrayListOf<String>("▼","신고", "수정", "삭제")
        //  val spinner1 = arrayListOf<String>("신고", "수정", "삭제")
        val spinner2 = arrayListOf<String>("▼","신고")

        val spinner1_Parent =
            ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, spinner1)
        val spinner2_Parent =
            ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, spinner2)
        if (writeuid.equals(youuid)) {
            binding.spinner.adapter = spinner1_Parent
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if(p2 == 0){

                    }
                    if (p2 == 1) {
//                        val intent = Intent(this@BoardInsideActivity, BoardDeclarationActivity::class.java)
//                        startActivity(intent)
                    }
                    if (p2 == 2) {
                        editPage(temp_keys)
                    }
                    if (p2 == 3) {
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
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
        else {
            binding.spinner.adapter = spinner2_Parent
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {

                    }
                    if(p2==1){
                        val intent = Intent(this@HotBoardInsideActivity, BoardDeclarationActivity::class.java)
                        startActivity(intent)
//                        val intent = Intent(this@BoardInsideActivity,BoardDeclarationActivity::class.java)
//                        intent.putExtra("data",temp_keys)
//                        startActivity(intent)
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
                // commentCountList.distinct()
                //commentCountList.toSet()
                // commentCountList.reverse()
                //newCountList= commentCountList.distinct() as MutableList<String>
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
        //  val colist = commentCountList.distinct()
        Log.d("co_list",commentCountList.toString())
        //    Log.d("colist2",colist.toString())
        val mymykey = FBRef.mycommentRef.push().key.toString()
//        val coUid = "익명"+commentCountList.indexOf(FBAuth.getUid())
//        val mykey = FBRef.commentRef.push().key.toString()
        FBRef.mycommentRef
            .child(key)
            .child(FBAuth.getUid()).child(mymykey)
            .setValue(MycommentModel(key,datas2.userUid, datas2.title, datas2.contents, datas2.time))
    }

    private fun comment(key: String) {

//넘버db를 만들어서 넘버DB에 있는 COMMENTUID와 현재uid를 비교해서 같으면 FBRef.num.child(key).setvaule("번호",uid)

        //commentCountList.add(FBAuth.getUid())
//            commentCount(key)
        // commentCheck(key)
        val commentTitle = binding.commentArea.text.toString()
        val commentCreatedTime = FBAuth.getTime()
        val commenteryUid = FBAuth.getUid()
        //   val colist = commentCountList.distinct()
//            Log.d("colist",commentCountList.toString())
//            Log.d("colist2",colist.toString())
        // Log.d("newlist",newCountList.toString())
        commentCountList.distinct()
        commentCountList.reversed()

        val mymykey = FBRef.mycommentRef.push().key.toString()
        val coUid = "익명"+commentCountList.indexOf(FBAuth.getUid())
        val mykey = FBRef.commentRef.push().key.toString()
        Log.d("colist2",commentCountList.toString())


        FBRef.commentRef
            .child(mykey)
            .setValue(CommentModel(commentTitle, commentCreatedTime, coUid, key))
        Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
        //Log.d("colist",colist.toString())
        binding.commentArea.setText("")

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
        intent.putExtra("temp_key", datas2.key)
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
                    //  if (key.equals(item?.boardKeydari)) {
                    alllikeList.add(item!!.toString())
                    if (FBAuth.getUid().equals(item?.userUid)) {
                        likeList.add((item?.userUid.toString()))
                        // binding.likeBtn.isSelected = true
                    } else {
                        // binding.likeBtn.isSelected = false
                    }
                    //}
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
                    FBRef.likeboardRef.child(key).setValue(LikeBoardModel(key, FBAuth.getUid(), datas2.title, datas2.contents, datas2.time))
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
            .setValue(ScrapModel(key, FBAuth.getUid(), title, contents, time))
    }

    private fun unscrap(key: String) {
        FBRef.scrapboardRef.child(key).child(FBAuth.getUid()).removeValue()
    }

}