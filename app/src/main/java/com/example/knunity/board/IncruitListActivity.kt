package com.example.knunity.board

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.Incruit.IncruitAdapter
import com.example.knunity.Incruit.IncruitModel
import com.example.knunity.crawling.Data
import com.example.knunity.crawling.ItemTouchHelperCallback
import com.example.knunity.databinding.ActivityBoardListBinding
import com.example.knunity.databinding.ActivityIncruitListBinding
import com.example.knunity.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.*
import org.jsoup.Jsoup

class IncruitListActivity : AppCompatActivity() {
    private val data = arrayListOf<String>()
    private val binding: ActivityIncruitListBinding by lazy {
        ActivityIncruitListBinding.inflate(layoutInflater)
    }
    private val myRecyclerViewAdapter: IncruitAdapter by lazy {
        IncruitAdapter()
    }
    private val boardDataList = mutableListOf<IncruitModel>()
    private val boardKeyList = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        addData()
        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(binding.rvList))
        itemTouchHelper.attachToRecyclerView(binding.rvList)
        binding.rvList.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = myRecyclerViewAdapter
        }
    }
    private fun addData()
    {
            CoroutineScope(Dispatchers.IO).launch {
                //결과값이 있는 job -> deferred
                val check = async { crawlingData() }

                withContext(Dispatchers.Main) {
                    //async로 받았기때문에 await() 해줘야함.
                    myRecyclerViewAdapter.submitList(check.await().distinct())
                }

            }
        }
    private suspend fun crawlingData(): ArrayList<IncruitModel> {
        val dataList = arrayListOf<IncruitModel>()

        try {
            val doc = Jsoup.connect("https://knujob.knu.ac.kr/").get()

            val numElements = doc.select(".num")
            val titleElements = doc.select(".subject")

            for (i in 0 until numElements.size) {
                val num = numElements[i].text()
                val title = titleElements[i].text()

                val item = IncruitModel(num, title)
                dataList.add(item)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dataList
    }
//    private fun getFBBoardData() {
//        val postListener = object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                boardDataList.clear()
//                for (dataModel in snapshot.children) {
//                    Log.d("check", dataModel.toString())
//                    val item = dataModel.getValue(IncruitModel::class.java)
//                    Log.d("item",item.toString())
//                    boardDataList.add(item!!)
//                    boardKeyList.add(dataModel.key.toString()) //키값을 전달받는다.
//                }
//                boardKeyList.reverse() //파이어베이스 이용 시에 필요
//                boardDataList.reverse()
//                myRecyclerViewAdapter.submitList(boardDataList.toList())
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.w("check", "loadPost:onCancelled", error.toException())
//            }
//        }
//        FBRef.incruitRef.addValueEventListener(postListener)
//    }
}