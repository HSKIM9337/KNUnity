package com.example.knunity.board

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.knunity.databinding.ActivityBoardWriteBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.FBRef.Companion.userRef
import com.example.knunity.utils.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class BoardWriteActivity : AppCompatActivity() {
    private val binding: ActivityBoardWriteBinding by lazy {
        ActivityBoardWriteBinding.inflate(layoutInflater)
    }
    private lateinit var nick : String
    private var isFileUpload = false
    private var selectedFileUri: Uri? = null
    private var isVideoSelected = false
    //private var isImageUpload = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val getFile = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val uri = result.data?.data
                if (uri != null) {
                    selectedFileUri = uri
                    isFileUpload = true
                    if (uri.toString().contains("image")) {
                        // If image file selected, show in imageView
                        binding.imageArea.setImageURI(uri)
                        binding.imageArea.visibility = View.VISIBLE
                        binding.videoView.visibility = View.GONE
                        binding.webView.visibility = View.GONE
                    } else if (uri.toString().contains("video")) {
                        // If video file selected, show in videoView
                        binding.videoView.setVideoURI(uri)
                        binding.videoView.start()
                        binding.imageArea.visibility = View.GONE
                        binding.videoView.visibility = View.VISIBLE
                        binding.webView.visibility = View.GONE
                        isVideoSelected = true
                    } else {
                        // If animated gif selected, show in webView
                        binding.webView.loadUrl(uri.toString())
                        binding.imageArea.visibility = View.GONE
                        binding.videoView.visibility = View.GONE
                        binding.webView.visibility = View.VISIBLE
                    }
                }
            }
        }
//
        FBRef.userRef.child(FBAuth.getUid()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userModel = snapshot.getValue(UserModel::class.java)
                nick = userModel?.nickname.toString() // 가져온 닉네임 정보

                // 가져온 닉네임 정보를 사용하여 필요한 작업 수행
            }

            override fun onCancelled(error: DatabaseError) {
                // 에러 발생시 처리
                Log.w("FirebaseTest", "Failed to read value.", error.toException())
            }
        })
        binding.imageArea.setOnClickListener {
            // Launch the file picker
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            val chooser = Intent.createChooser(intent, "Select File")
            getFile.launch(chooser)
        }
        binding.webView.setOnClickListener{
            // Launch the file picker
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            val chooser = Intent.createChooser(intent, "Select File")
            getFile.launch(chooser)
        }
        binding.videoView.setOnClickListener{
            // Launch the file picker
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            val chooser = Intent.createChooser(intent, "Select File")
            getFile.launch(chooser)
        }


        write()
        onBackPressed()
    }


private fun fileUpload(key: String, uri: Uri) {
    val storage = Firebase.storage
    val storageRef = storage.reference
    val ext = when {
        uri.toString().contains("image") -> "png"
        uri.toString().contains("video") -> "mp4"
        uri.toString().contains("gif") -> "gif"  // GIF 파일 추가
        else -> ""
    }

    if (ext.isNotEmpty()) {
        val fileRef = storageRef.child("$key.$ext")

        val uploadTask = fileRef.putFile(uri)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }
    }
}

    private fun write() {
        binding.writebtn.setOnClickListener {
            val title = binding.titleArea.text.toString()
            val contents = binding.contentArea.text.toString()
            val uid = FBAuth.getUid()
            val time = FBAuth.getTime()
            val key = FBRef.boardRef.push().key.toString()

            FBRef.boardRef
                .child(key)
                .setValue(BoardModel("자유게시판",key,uid, title, contents, time,nick))
            //이미지의 이름을 문서의 key값으로 해줘서 이미지에 대한 정보를 찾기쉽게 해놓음
            Toast.makeText(this, "게시글을 썼습니다", Toast.LENGTH_SHORT).show()
                if (isFileUpload && selectedFileUri  != null) {
                    fileUpload(key, selectedFileUri !!)
                }
            finish()
               // Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onBackPressed() {
        binding.writeBack.setOnClickListener {
            startActivity(Intent(this, BoardListActivity::class.java))
            finish()
        }
    }
}