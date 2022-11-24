package com.example.knunity.board

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.knunity.databinding.ActivityBoardInsideBinding
import com.example.knunity.utils.FBRef
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class BoardInsideActivity : AppCompatActivity() {
    private lateinit var key: String
    lateinit var datas: BoardModel
    private val binding: ActivityBoardInsideBinding  by lazy {
        ActivityBoardInsideBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        datas = intent.getSerializableExtra("data") as BoardModel

        binding.titleArea.text = datas.title
        binding.contentArea.text = datas.contents
        binding.timeArea.text = datas.time
        val key = FBRef.boardRef.key.toString()


        val storageReference = Firebase.storage.reference
        val storages = FirebaseStorage.getInstance()


// ImageView in your Activity
        val imageView = binding.getImageArea

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
            .load(storageReference)
            .into(imageView)





    }


}

