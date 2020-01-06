package com.music961.chat.recycler_adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.music961.chat.Activity.ChatRoomActivity
import com.music961.chat.R

class ChatListAdapter : RecyclerView.Adapter<ChatListAdapter.MainRecyclerHolder>() {
    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerHolder {
        context = parent.context
        return MainRecyclerHolder(LayoutInflater.from(parent.context).inflate(R.layout.chat_list_adapter, parent, false))
    }

    //리사이클러뷰에 나타낼 item 수 여기는 최대 5개라고 표기
    override fun getItemCount(): Int {
        return 5
    }

    //리사이클러 뷰의 item의 텍스트 설정
    override fun onBindViewHolder(h: MainRecyclerHolder, p: Int) {
        h.tv1.text = "${h.tv1.text} $p"

        h.itemView.setOnClickListener {
           val intent = Intent(context, ChatRoomActivity::class.java)
           context.startActivity(intent)

        }
    }

    inner class MainRecyclerHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val tv1 = itemview.findViewById<TextView>(R.id.tv_1)

    }
}