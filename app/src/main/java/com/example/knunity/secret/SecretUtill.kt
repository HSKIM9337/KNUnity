package com.example.knunity.secret

import androidx.recyclerview.widget.DiffUtil
import com.example.knunity.board.BoardModel

class SecretUtill : DiffUtil.ItemCallback<SecretBoardModel>() {
    override fun areItemsTheSame(oldItem: SecretBoardModel, newItem: SecretBoardModel): Boolean {

        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: SecretBoardModel, newItem: SecretBoardModel): Boolean {

        return oldItem == newItem
    }
}