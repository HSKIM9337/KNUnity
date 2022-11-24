package com.example.knunity.board

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.knunity.comment.CommentModel
import com.example.knunity.databinding.ActivityBoardInsideBinding
import com.example.knunity.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_board_inside.*

class BoardInsideActivity : AppCompatActivity() {
    private lateinit var key: String
    private val binding: ActivityBoardInsideBinding by lazy {
        ActivityBoardInsideBinding.inflate(layoutInflater)
    }
    lateinit var datas: BoardModel
    private val Tag = BoardInsideActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // val title = intent.getStringArrayExtra("title").toString()
        //val contents = intent.getStringArrayExtra("contents").toString()
        //val time = intent.getStringArrayExtra("time").toString()
        //val uid = intent.getStringArrayExtra("uid").toString()
        
        datas = intent.getSerializableExtra("data") as BoardModel
        binding.titlePage.text = datas.title
        binding.contentPage.text = datas.contents
        binding.timePage.text = datas.time
        val temp_keys=datas.key
        key = intent.getStringExtra(temp_keys).toString()
        Log.d("test", temp_keys)
        //getBoardDate(key)
        Toast.makeText(this,temp_keys,Toast.LENGTH_SHORT).show()
        getImagefromFB(temp_keys+".png")
        deleteWrite(temp_keys)

        comment()
        editPage(temp_keys)

        //  Log.d(Tag, title)
        // Log.d(Tag, contents)
        // Log.d(Tag, time)


        val menus = arrayOf("수정","신고")
        val spinnAdapter : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,menus)
        val menuSpinner = binding.spinner
        menuSpinner.adapter = spinnAdapter

    }
    private fun comment()
    {   binding.commentBtn.setOnClickListener {
        insertComment(key)
    }
    }
    fun insertComment(key: String)
    {
        FBRef.commentRef
            .child(key)
            .push()
            .setValue(CommentModel(binding.commentArea.text.toString()))
        Toast.makeText(this,"댓글 입력 완료",Toast.LENGTH_SHORT).show()
        binding.commentArea.setText("")
    }
//
//    private fun getBoardDate(key: String)
//    {/
//        val postListener = object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val dataInside1 = snapshot.getValue(BoardModel::class.java)
//                Log.d(Tag, dataInside1!!.title)
//                binding.titlePage.text = dataInside1!!.title
//                binding.contentPage.text = dataInside1!!.contents
//                binding.timePage.text = dataInside1!!.time
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d(Tag,"load Post:onCancelled", error.toException())
//            }
//        }
//        FBRef.boardRef.child(key).addValueEventListener(postListener)
//    }

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
    private fun spinner() {

        binding.spinner.setOnClickListener {

        }


    }
}