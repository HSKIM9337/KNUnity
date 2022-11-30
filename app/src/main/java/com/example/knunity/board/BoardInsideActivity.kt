package com.example.knunity.board

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
    private val myRecyclerViewAdapter: CommentAdapter by lazy {
        CommentAdapter()
    }
    //private val boardDataList = mutableListOf<BoardModel>()
    lateinit var datas: BoardModel
    private val commentDataList = mutableListOf<CommentModel>()
    private val commentKeyList = mutableListOf<String>()
    private val Tag = BoardInsideActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        
        datas = intent.getSerializableExtra("data") as BoardModel
        binding.titlePage.text = datas.title
        binding.contentPage.text = datas.contents
        binding.timePage.text = datas.time
        val temp_keys=datas.key
        key = intent.getStringExtra(temp_keys).toString()
        Log.d("test", temp_keys)
        getBoardData(temp_keys)
        onBackPressed()
        Toast.makeText(this,temp_keys,Toast.LENGTH_SHORT).show()
        getImagefromFB(temp_keys+".png")
        deleteWrite(temp_keys)
       // useRV()//데이터 저장이 이상하게되어서 안씀
        comment(key)
        editPage(temp_keys)

        //  Log.d(Tag, title)
        // Log.d(Tag, contents)
        // Log.d(Tag, time)


        val menus = arrayOf("수정","신고")
        val spinnAdapter : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,menus)
        val menuSpinner = binding.spinner
        menuSpinner.adapter = spinnAdapter
        getCommentData()
    }
    private fun comment(key:String)
    {   binding.commentBtn.setOnClickListener {
        insertComment(key)
    }
    }
    private fun insertComment(key: String)
    {
        val titleco = binding.commentArea.text.toString()
        val timeco = FBAuth.getTime()
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(titleco,timeco)
        Toast.makeText(this,"댓글 입력 완료",Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }
//    private fun useRV() {
//        binding.rvList.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTbICAL, false)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
//            adapter = myRecyclerViewAdapter
//        }
//
//    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(BoardModel::class.java)
               // binding.titlePage.setText(dataModel?.title)
               // binding.commentArea.setText(dataModel?.contents)
                val myUid = FBAuth.getUid()
                val writeUid = dataModel?.uid

                if(myUid.equals(writeUid))
                {
                    binding.updateBt.isVisible=true
                    binding.deleteBt.isVisible=true

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun deleteWrite(key: String){
        binding.deleteBt.setOnClickListener {
            try {
                FBRef.boardRef.child(key).removeValue()
                Toast.makeText(this, "삭제완료", Toast.LENGTH_SHORT).show()
                finish()
            }
            catch (e: StorageException){
                Toast.makeText(this, "오류",Toast.LENGTH_SHORT).show()
            }
            }
    }
    private fun editPage(key: String) {
        binding.updateBt.setOnClickListener{
           // FBRef.boardRef.child(key).setValue(BoardModel(binding.titlePage.text.toString(),binding.contentPage.text.toString(),binding.timePage.toString()))

            val intent = Intent(this, BoardEditActivity::class.java)
            intent.putExtra("temp_key",datas.key)
            startActivity(intent)

            Toast.makeText(this,"수정 완료",Toast.LENGTH_SHORT).show()
            //finish()

            }
    }
    private fun getCommentData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              commentDataList.clear()
                for (dataModel in snapshot.children) {
                    Log.d("check", dataModel.toString())

                    val item = dataModel.getValue(CommentModel::class.java)
                    commentDataList.add(item!!)
                    commentKeyList.add(dataModel.key.toString()) //키값을 전달받는다.
                }
                commentKeyList.reverse() //파이어베이스 이용 시에 필요
                commentDataList.reverse()
                myRecyclerViewAdapter.submitList(commentDataList.toList())
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("check", "loadPost:onCancelled", error.toException())
            }
        }
        FBRef.commentRef.addValueEventListener(postListener)
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
                binding.imagePage.isVisible=false
                imageViewFromFB.isVisible = false
                Toast.makeText(this,key,Toast.LENGTH_SHORT).show()
            }

        })
    }
//    private fun getImageData(key: String) {
//        // Reference to an image file in Cloud Storage
//        val storageReference = Firebase.storage.reference.child(key + ".png")
//        // ImageView in your Activity
//        val imageViewFB = binding.imagePage
//
//
//
//        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Glide.with(this)
//                    .load(task.result)
//                    .into(imageViewFB)
//            } else {
//                Toast.makeText(this, "FAIL TO LOAD", Toast.LENGTH_SHORT).show()
//            }
//        })
//
//    }
override fun onBackPressed() {
    binding.writeBack.setOnClickListener {
        startActivity(Intent(this, BoardListActivity::class.java))
        finish()
    }
}
    private fun spinner() {

        binding.spinner.setOnClickListener {

        }


    }
}