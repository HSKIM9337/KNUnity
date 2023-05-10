package com.example.knunity.calendar

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.knunity.Fragments.HomeFragment
import com.example.knunity.databinding.ActivityMemoBinding
import com.example.knunity.utils.FBAuth
import com.example.knunity.utils.FBRef
import com.example.knunity.utils.FBRef.Companion.memoRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

class MemoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMemoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val selectedDate = intent.getParcelableExtra<CalendarDay>("selected_date")
        binding.dateTextView.text = selectedDate?.date.toString()
        Log.d("date",binding.dateTextView.text.toString())
        val uid = FBAuth.getUid()
        val memoRef = FBRef.memoRef.child(uid)
        memoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(binding.dateTextView.text.toString())) {
                    binding.memoEditText.setText(snapshot.child(binding.dateTextView.text.toString()).child("memo").value.toString())
                } else {
                    binding.memoEditText.setText("")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
        binding.saveButton.setOnClickListener {
            saveMemo()
        }
        onBackPressed()
    }

    private fun saveMemo() {
        val memo = binding.memoEditText.text.toString()
        if (memo.isBlank()) {
            // Memo is empty, do not save to Firebase
            return
        }
        val date = binding.dateTextView.text.toString()
        val uid = FBAuth.getUid()

        val memoryRef = FBRef.memoRef.child(uid)
        memoryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(date)) {
                    // 이미 해당 날짜에 메모가 있는 경우 업데이트합니다.
                    memoryRef.child(date).child("memo").setValue(memo)
                } else {
                    // 해당 날짜에 메모가 없는 경우 새로 추가합니다.
                    memoryRef.child(date).setValue(Memo(uid, date, memo))
                }
                setResult(Activity.RESULT_OK)
                finish()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled", error.toException())
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
        })
    }
    override fun onBackPressed() {
        binding.cancelButton.setOnClickListener {
            //startActivity(Intent(this, HomeFragment::class.java))
            finish()
        }
    }
}