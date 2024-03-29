package com.example.knunity.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.knunity.Incruit.IncruitModel
import com.example.knunity.databinding.ActivityBoardWriteBinding
import com.example.knunity.databinding.ActivityIncruitWriteBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class IncruitWriteActivity : AppCompatActivity() {
    private val binding: ActivityIncruitWriteBinding by lazy {
        ActivityIncruitWriteBinding.inflate(layoutInflater)
    }
    private var isImageUpload = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {

                binding.imageArea.setImageURI(it)
            }
        )
//
        binding.imageArea.setOnClickListener {
            getImage.launch("image/*")
            isImageUpload = true
        }
//        imageUp()
        write()
        onBackPressed()
    }

//    private fun imageUp() {
//        binding.imageArea.setOnClickListener {
//            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//     //       startActivityForResult(gallery, 10)
//            getImage.lanuch("image/*")
//
//            isImageUpload = true
//        }
//    }

    private fun imageupload(key: String) {
        // Get the data from an ImageView as bytes
        val storage = Firebase.storage
        val storageRef = storage.reference

// Create a reference to "mountains.jpg"
        val mountainsRef = storageRef.child(key + ".png")
        val imageView = binding.imageArea
//        imageView.isDrawingCacheEnabled = true
//        imageView.buildDrawingCache()
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

    private fun write() {
        binding.writebtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val contents = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val key = FBRef.incruitRef.push().key.toString()

            FBRef.incruitRef
                .child(key)
                .setValue(IncruitModel(key,uid, title, contents, time))
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
            startActivity(Intent(this, IncruitListActivity::class.java))
            finish()
        }
    }
}