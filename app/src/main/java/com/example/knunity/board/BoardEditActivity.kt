package com.example.knunity.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.knunity.databinding.ActivityBoardEditBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardEditActivity : AppCompatActivity() {
    private lateinit var key: String
    private var isImageUpload = false
    private val binding: ActivityBoardEditBinding by lazy {
        ActivityBoardEditBinding.inflate(layoutInflater)
    }
    private lateinit var datas: BoardModel
    private val Tag = BoardEditActivity::class.java.simpleName
    private lateinit var writerUid: String
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.imageArea.setImageURI(it)
            }
        )
        binding.imageArea.setOnClickListener {
            getImage.launch("image/*")
            isImageUpload = true
        }
        key = intent.getStringExtra("temp_key").toString()

        getImagefromFB(key)
        getBoardData(key)
        onBackPressed()
        binding.editBtn.setOnClickListener {
            editBoardData(key)
            intent = Intent(this, BoardListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun editBoardData(key: String) {
        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(
                    key,
                    writerUid,
                    binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    FBAuth.getTime()
                //      .setValue(BoardModel(key,uid, title, contents, time))
                )
            )
        if (isImageUpload) {
            imageupload(key)
            finish()
        } else {
            finish()
            // Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
        }

        //   finish()

    }

    private fun getBoardData(key: String) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(BoardModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.contents)
                writerUid = dataModel!!.uid

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

    private fun getImagefromFB(key: String) {

        val storageReference = Firebase.storage.reference.child(key + ".png")
        val imageViewFromFB = binding.imageArea


        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)
            } else {
                imageViewFromFB.isVisible = false
                Toast.makeText(this, key, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun imageupload(key: String) {
        // Get the data from an ImageView as bytes
        val storage = Firebase.storage
        val storageRef = storage.reference


        val mountainsRef = storageRef.child(key + ".png")

        val imageView = binding.imageArea
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = (imageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos)
        val data = baos.toByteArray()

        var uploadTask = mountainsRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }


    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && requestCode == 10) {
//            binding.imageArea.setImageURI(data?.data)
//        }
//    }

    private fun write(key: String) {
        binding.editBtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val contents = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel(key, uid, title, contents, time))
            //이미지의 이름을 문서의 key값으로 해줘서 이미지에 대한 정보를 찾기쉽게 해놓음
            Toast.makeText(this, "게시글을 썼습니다", Toast.LENGTH_SHORT).show()
            if (isImageUpload) {
                imageupload(key)
                finish()
            } else {
                finish()
                // Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onBackPressed() {
        binding.writeBack.setOnClickListener {
            startActivity(Intent(this, BoardListActivity::class.java))
            finish()
        }
    }

}