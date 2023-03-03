package com.example.knunity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.knunity.crawling.CrawlRecyclerAdapter
import com.example.knunity.crawling.Data
import com.example.knunity.crawling.ItemTouchHelperCallback
import com.example.knunity.databinding.FragmentUniqueBinding
import kotlinx.coroutines.*
import org.jsoup.Jsoup


class UniqueFragment : Fragment() {
    private val data = arrayListOf<String>()
    private val crawlRecyclerAdapter: CrawlRecyclerAdapter by lazy {
        CrawlRecyclerAdapter()
    }
    private val datas = String
    private var _binding: FragmentUniqueBinding? = null
    private val binding: FragmentUniqueBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUniqueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addData()
        val itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(binding.keywordRV))
        itemTouchHelper.attachToRecyclerView(binding.keywordRV)
        binding.keywordRV.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = crawlRecyclerAdapter
        }


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun addData() {
        binding.keywordCheck.setOnClickListener {
            if (data != null) {
                if (data.size < 4) {
                    if (binding.keywordCheck.text.length < 10) {
                        data.add(binding.keywordCheck.text.toString())
                        val datas = data.joinToString(" ")
                        binding.keywordTVS.setText(datas.toString())
                        binding.keywordCheck.text.clear()
                    } else {
                        Toast.makeText(context, "키워드는 10자를 넘어 갈 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "키워드는 최대 3개까지만 등록가능합니다", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "키워드는 공백은 등록 불가능합니다", Toast.LENGTH_SHORT).show()
            }
            CoroutineScope(Dispatchers.IO).launch {
                //결과값이 있는 job -> deferred
                val check = async { crawlingData() }

                withContext(Dispatchers.Main) {
                    //async로 받았기때문에 await() 해줘야함.
                    crawlRecyclerAdapter.submitList(check.await().distinct())
                }

            }
        }
    }

    private suspend fun crawlingData(): ArrayList<Data> {
        val dataList = arrayListOf<Data>()

        try {
            val doc =
                Jsoup.connect("https://www.knu.ac.kr/wbbs/wbbs/bbs/btin/list.action?bbs_cde=1&menu_idx=67")
                    .get()

            for (i: Int in 5..15) {
                val num = doc.select(".num")[i].text().toString()
                val title = doc.select(".subject")[i].text().toString()
                for (j: Int in 0..4) {
                    if (title.contains(data[j])) {
                        var items = Data(num, title)
                        dataList.add(items)
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dataList
    }
}