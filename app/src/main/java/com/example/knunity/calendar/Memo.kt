package com.example.knunity.calendar

//@Entity(tableName = "memo_table")
data class Memo(
  //  @PrimaryKey(autoGenerate = true)
    val uid: String="",
    val date: String="",
    val memo: String=""
)
