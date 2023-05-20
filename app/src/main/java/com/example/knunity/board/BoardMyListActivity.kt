package com.example.knunity.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.databinding.ActivityBoardListBinding
import com.example.knunity.databinding.ActivityBoardMyListBinding
import com.example.knunity.mylist.MylistAdapter
import com.example.knunity.mylist.MylistModel
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardMyListActivity : AppCompatActivity() {
    private val binding: ActivityBoardMyListBinding by lazy {
        ActivityBoardMyListBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter: MylistAdapter by lazy {
        MylistAdapter()
    }
    private val boardDataList = mutableListOf<MylistModel>()
    private val boardKeyList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        useRV()
        getFBBoardData()
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
            val intent = Intent(this, BoardMyInsideActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardDataList.clear()
                for (dataModel in snapshot.children) {
                    Log.d("check", dataModel.toString())
                    val item = dataModel.getValue(MylistModel::class.java)
                    Log.d("item", item.toString())
                    if (item?.userUid.equals(FBAuth.getUid())) {
                        boardDataList.add(item!!)
                        boardKeyList.add(dataModel.key.toString()) //키값을 전달받는다.
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
        FBRef.myref.addValueEventListener(postListener)
    }
}