package com.example.knunity.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.databinding.ActivityBoardLikeListBinding
import com.example.knunity.databinding.ActivityBoradScrapListBinding
import com.example.knunity.hotboard.LikeBoardModel
import com.example.knunity.hotboard.LikeRecyclerAdapter
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardLikeListActivity : AppCompatActivity() {
        private val binding: ActivityBoardLikeListBinding by lazy {
            ActivityBoardLikeListBinding.inflate(layoutInflater)
//    ActivityBoardListBinding.inflate(layoutInflater)
        }
        private val myRecyclerViewAdapter: LikeRecyclerAdapter by lazy {
            LikeRecyclerAdapter()
            //  RecyclerAdapter()
        }
        //lateinit var datas: ScrapModel
        private val scrapList = mutableListOf<String>()
        private val boardDataList = mutableListOf<LikeBoardModel>()
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
            binding.rvList4.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = myRecyclerViewAdapter
            }
        }
        private fun moveWrite() {
            binding.rvList4.setOnClickListener{
                val intent = Intent(this, BoardInsideActivity::class.java)
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
                        val item2 = dataModel.getValue(LikeBoardModel::class.java)
                            boardDataList.add(item2!!)
                            boardKeyList.add(dataModel.key.toString())
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
            FBRef.likeboardRef.addValueEventListener(postListener)
        }
    }