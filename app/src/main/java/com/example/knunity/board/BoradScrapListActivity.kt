package com.example.knunity.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.databinding.ActivityBoardListBinding
import com.example.knunity.databinding.ActivityBoradScrapListBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


class BoradScrapListActivity : AppCompatActivity() {
//null일때 문구 하나(스크랩 한게 없습니다.) 있는 레이아웃 해야 할듯
private val binding: ActivityBoradScrapListBinding by lazy {
   ActivityBoradScrapListBinding.inflate(layoutInflater)
//    ActivityBoardListBinding.inflate(layoutInflater)
}
    private val myRecyclerViewAdapter: ScrapRecyclerAdapter by lazy {
        ScrapRecyclerAdapter()
      //  RecyclerAdapter()
    }
    //lateinit var datas: ScrapModel
    private val scrapList = mutableListOf<String>()
    private val boardDataList = mutableListOf<ScrapModel>()
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
        binding.rvList3.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = myRecyclerViewAdapter
        }
    }
    private fun moveWrite() {
        binding.rvList3.setOnClickListener{
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
                    val item2 = dataModel.child(FBAuth.getUid()).getValue(ScrapModel::class.java)
                    Log.d("item2", dataModel.child(FBAuth.getUid()).getValue(ScrapModel::class.java).toString())
                    Log.d("value",dataModel.value.toString())

     //               Log.d("scrap",FBRef.scrapboardRef.child(dataModel.))
                    Log.d("what",FBRef.scrapboardRef.child(dataModel.key.toString()).toString())
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
        FBRef.scrapboardRef.addValueEventListener(postListener)
    }
}