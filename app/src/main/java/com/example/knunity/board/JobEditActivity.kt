package com.example.knunity.board

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.knunity.Fragments.BoardFragment
import com.example.knunity.databinding.ActivityBoardEditBinding
import com.example.knunity.databinding.ActivityJobEditBinding
import com.example.knunity.job.JobModel
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class JobEditActivity : AppCompatActivity() {
    private lateinit var key: String
    private var isFileUpload = false
    private var selectedFileUri: Uri? = null
    private var isVideoSelected = false
    private lateinit var nick : String
    private val binding: ActivityJobEditBinding by lazy {
        ActivityJobEditBinding.inflate(layoutInflater)
    }
    private lateinit var datas: JobModel
    private val Tag = BoardEditActivity::class.java.simpleName
    private lateinit var writerUid: String
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
        key = intent.getStringExtra("temp_key").toString()

        getImagefromFB(key)
        getBoardData(key)
        onBackPressed()
        binding.editBtn.setOnClickListener {
            editBoardData(key)
            intent = Intent(this, JobListActivity::class.java)
            Toast.makeText(this, "수정완료", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }

    private fun editBoardData(key: String) {
        FBRef.jobRef
            .child(key)
            .setValue(
                JobModel(
                    "취업게시판", key, writerUid, binding.titleArea.text.toString(), binding.contentArea.text.toString(), FBAuth.getTime(),nick)
            )
        if (isFileUpload && selectedFileUri  != null) {
            fileUpload(key, selectedFileUri !!)
        } else {
            finish()
            // Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
        }

        //   finish()

    }

    private fun getBoardData(key: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataModel = snapshot.getValue(JobModel::class.java)
                binding.titleArea.setText(dataModel?.title)
                binding.contentArea.setText(dataModel?.contents)
                writerUid = dataModel!!.uid
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        FBRef.jobRef.child(key).addValueEventListener(postListener)
    }

    private fun getImagefromFB(key: String) {
        val storageReference = Firebase.storage.reference
        val imageViewFromFB = binding.imageArea
        val videoViewFromFB = binding.videoView
        val gifViewFromFB = binding.webView
        storageReference.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->

                if (item.name.substring(0, item.name.length - 4) == key) {
                    // key와 일치하는 파일을 찾음
                    val extension = item.name.takeLast(4).lowercase() // 파일 이름에서 마지막 4글자 추출
                    Log.d("extension", extension)

                    if (extension == ".png") {
                        // 이미지 처리 로직
                        imageViewFromFB.isVisible=true
                        videoViewFromFB.isVisible=false
                        gifViewFromFB.isVisible=false
                        storageReference.child(key+".png").downloadUrl.addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                Log.d("check",task.isSuccessful.toString())
                                Glide.with(this)
                                    .load(task.result)
                                    .into(imageViewFromFB)
                            } else {
                                imageViewFromFB.isVisible = true
                                videoViewFromFB.isVisible = false
                                gifViewFromFB.isVisible = false
                                Toast.makeText(this, key, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else if (extension == ".mp4") {
                        // 비디오 처리 로직
                        videoViewFromFB.isVisible = true
                        imageViewFromFB.isVisible = false
                        gifViewFromFB.isVisible = false
                        storageReference.child(key+".mp4").downloadUrl.addOnCompleteListener(
                            OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                videoViewFromFB.setVideoURI(task.result)
                                videoViewFromFB.start()
                            } else {
                                imageViewFromFB.isVisible = false
                                videoViewFromFB.isVisible = true
                                gifViewFromFB.isVisible = false
                                Toast.makeText(this, "Failed to load content", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else if (extension == ".gif") {
                        // 움짤 처리 로직
                        gifViewFromFB.isVisible = true
                        imageViewFromFB.isVisible = false
                        videoViewFromFB.isVisible = false
                        gifViewFromFB.loadUrl(storageReference.toString())
                    }
                }
            }
        }.addOnFailureListener { exception ->
            // 실패 처리 로직
        }

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


    override fun onBackPressed() {
        binding.writeBack.setOnClickListener {
            finish()
        }
    }

}