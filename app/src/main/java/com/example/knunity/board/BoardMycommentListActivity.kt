package com.example.knunity.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.comment.CommentBoardModel
import com.example.knunity.comment.MycommentAdapter
import com.example.knunity.comment.MycommentModel
import com.example.knunity.databinding.ActivityBoardMycommentListBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardMycommentListActivity : AppCompatActivity() {
    private val binding: ActivityBoardMycommentListBinding by lazy {
        ActivityBoardMycommentListBinding.inflate(layoutInflater)
        //ActivityBoardLikeListBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter: MycommentAdapter by lazy {
        MycommentAdapter()

    }
    //lateinit var datas: ScrapModel
    private val scrapList = mutableListOf<String>()
    private val boardDataList = mutableListOf<CommentBoardModel>()
    private val boardKeyList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //    datas = intent.getSerializableExtra("data") as ScrapModel
        getFBBoardData()
        useRV()
        //move_to()
        moveWrite()
    }

    private fun useRV() {
        binding.rvList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = myRecyclerViewAdapter
        }
    }
    private fun moveWrite() {
        binding.rvList.setOnClickListener{
            val intent = Intent(this, CommentBoardInsideActivity::class.java)
            startActivity(intent)
        }
    }


    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //scrapList.clear()
                boardDataList.clear()
                for (dataModel in snapshot.children) {
                    Log.d("data", dataModel.toString())
                    val item2 = dataModel.child(FBAuth.getUid()).getValue(CommentBoardModel::class.java)
                    if(item2?.userUid.equals(FBAuth.getUid())) {//이 조건 없으면 뭔가 db경로에서 오류가 나는듯
                        boardDataList.add(item2!!)
                        boardKeyList.add(dataModel.key.toString())
                    }
                }
                boardKeyList.reverse() //파이어베이스 이용 시에 필요
                boardDataList.reverse()
                myRecyclerViewAdapter.submitList(boardDataList.toList())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        //FBRef.scrapboardRef.addValueEventListener(postListener)
        FBRef.comboardRef.addValueEventListener(postListener)
    }
}