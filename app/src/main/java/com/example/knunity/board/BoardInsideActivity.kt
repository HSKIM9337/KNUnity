package com.example.knunity.board


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.knunity.comment.CommentAdapter
import com.example.knunity.comment.CommentModel
import com.example.knunity.databinding.ActivityBoardInsideBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage


class BoardInsideActivity : AppCompatActivity() {
    private lateinit var key: String
    private val binding: ActivityBoardInsideBinding by lazy {
        ActivityBoardInsideBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter2: CommentAdapter by lazy {
        CommentAdapter()
    }
    lateinit var datas: BoardModel
    private val commentDataList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()
    private val likeList = ArrayList<String>()
    private val scrapList= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        datas = intent.getSerializableExtra("data") as BoardModel
        binding.titlePage.text = datas.title
        binding.contentPage.text = datas.contents
        binding.timePage.text = datas.time
//        binding.likeCount.text=datas.likCount
//        binding.commentCount.text=datas.comCount
//        binding.scrapCount.text=datas.scrCount
        binding.likeBtn.setOnClickListener {
            likethis()
        }
        binding.scrapBtn.setOnClickListener {
            scrap()
        }
        val temp_keys = datas.key
        key = temp_keys
        val writeuid = datas.uid
        val youuid = FBAuth.getUid()
        val spinner1 = arrayListOf<String>("신고", "수정", "삭제")
        val spinner2 = arrayListOf<String>("신고")
        val spinner1_Parent =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinner1)
        val spinner2_Parent =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinner2)
        if (writeuid.equals(youuid)) {
            binding.spinner.adapter = spinner1_Parent
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 == 0) {

                    }
                    if (p2 == 1) {
                        editPage(temp_keys)
                    }
                    if (p2 == 2) {
                        try {
                            FBRef.boardRef.child(temp_keys).removeValue()
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
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        }
        Log.d("test", temp_keys)
        comment(temp_keys)
        getCommentData(temp_keys)
        onBackPressed()
        useRV2()
        getImagefromFB(temp_keys + ".png")
    }

    private fun getCommentData(key: String) {
//        binding.likeCount.text=datas.likCount
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                commentDataList.clear()
                for (dataModel in snapshot.children) {
                    val item = dataModel.getValue(CommentModel::class.java)
                    if (key.equals(item?.boardKeyda)) {
                        commentDataList.add(item!!)
                        commentKeyList.add(dataModel.key.toString()) //키값을 전달받는다.
                    }
                }
                binding.commentCount.text=commentDataList.size.toString()
                //datas.comCount = commentDataList.size.toString()
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

    private fun comment(key: String) {
        binding.commentBtn.setOnClickListener {
            Log.d("comment",commentDataList.size.toString())
            val commentTitle = binding.commentArea.text.toString()
            val commentCreatedTime = FBAuth.getTime()
            val commenteryUid = "익명" + (commentDataList.size+1).toString()
            val mykey = FBRef.commentRef.push().key.toString()
            FBRef.commentRef
                .child(mykey)
                .setValue(CommentModel(commentTitle, commentCreatedTime, commenteryUid, key))
            Toast.makeText(this, "댓글 입력 완료", Toast.LENGTH_SHORT).show()
            binding.commentArea.setText("")

        }
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
            startActivity(Intent(this, BoardListActivity::class.java))
            finish()
        }
    }

    private fun likethis() {

            if (likeList.contains(datas.uid))
            {
                 likeList.remove(datas.uid)
                binding.likeBtn.isSelected=false
            }
            else
            {
                likeList.add(datas.uid)
                binding.likeBtn.isSelected=true
            }
        binding.likeCount.text=likeList.size.toString()
//            datas.likCount=likeList.size.toString()
    }
    private fun scrap() {
        if (scrapList.contains(datas.uid))
        {
            scrapList.remove(datas.uid)
            binding.scrapBtn.isSelected=false
        }
        else
        {
            scrapList.add(datas.uid)
            binding.scrapBtn.isSelected=true
        }
        binding.scrapCount.text=scrapList.size.toString()
//        datas.scrCount=scrapList.size.toString()
    }
}