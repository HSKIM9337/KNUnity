package com.example.knunity.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.databinding.ActivityBoardListBinding
import com.example.knunity.databinding.ActivityBoardSecretListBinding
import com.example.knunity.secret.SecretAdapter
import com.example.knunity.secret.SecretBoardModel
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BoardSecretListActivity : AppCompatActivity() {
    private val binding: ActivityBoardSecretListBinding by lazy {
        ActivityBoardSecretListBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter: SecretAdapter by lazy {
        SecretAdapter()
    }
    private val boardDataList = mutableListOf<SecretBoardModel>()
    private val boardKeyList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        useRV()
        getFBBoardData()
        move_to()
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
            val intent = Intent(this, BoardSecretInsideActivity::class.java)
            startActivity(intent)
        }
    }

    private fun move_to() {
        binding.WriteIV.setOnClickListener {
            val intent = Intent(this, SecretWriteActivity::class.java)
            startActivity(intent)
        }
    }
    private fun getFBBoardData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                boardDataList.clear()
                for (dataModel in snapshot.children) {
                    Log.d("check", dataModel.toString())
                    val item = dataModel.getValue(SecretBoardModel::class.java)
                    Log.d("item",item.toString())
                    boardDataList.add(item!!)
                    boardKeyList.add(dataModel.key.toString()) //키값을 전달받는다.
                }
                boardKeyList.reverse() //파이어베이스 이용 시에 필요
                boardDataList.reverse()
                myRecyclerViewAdapter.submitList(boardDataList.toList())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.secretboardRef.addValueEventListener(postListener)
    }
}