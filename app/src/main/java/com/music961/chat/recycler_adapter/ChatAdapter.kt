package com.music961.yj_prac_1230.recycler_adapter

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.music961.chat.Bean.ChatModel
import com.music961.chat.Bean.myID
import com.music961.chat.R
import kotlinx.android.synthetic.main.item_my_chat.view.*
import kotlinx.android.synthetic.main.item_your_chat.view.*

class ChatAdapter(val context: Context, val arrayList: ArrayList<ChatModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    fun addItem(item: ChatModel) {//아이템 추가
        arrayList.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        //getItemViewType 에서 뷰타입 1을 리턴받았다면 내채팅레이아웃을 받은 Holder를 리턴
        return if(viewType == 1){
            view = LayoutInflater.from(context).inflate(R.layout.item_my_chat, parent, false)
            Holder(view)
        }
        //getItemViewType 에서 뷰타입 2을 리턴받았다면 상대채팅레이아웃을 받은 Holder2를 리턴
        else{
            view = LayoutInflater.from(context).inflate(R.layout.item_your_chat, parent, false)
            Holder2(view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(v: RecyclerView.ViewHolder, i: Int) {
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder라면 내채팅, item_my_chat의 뷰들을 초기화 해줌
        if (v is Holder) {
            v.chat_Text.text = arrayList[i].chat_msg
            v.chat_Time.text = arrayList[i].date_time
            v.chat_Text.gravity = Gravity.RIGHT
            v.chat_Time.gravity = Gravity.RIGHT

        }
        //onCreateViewHolder에서 리턴받은 뷰홀더가 Holder2라면 상대의 채팅, item_your_chat의 뷰들을 초기화 해줌
        else if(v is Holder2) {
            v.chat_You_Image.setImageResource(R.mipmap.ic_launcher)
            v.chat_You_Name.text = arrayList[i].name
            v.chat_Text2.text = arrayList[i].chat_msg
            v.chat_Time2.text = arrayList[i].date_time
            v.chat_Text2.gravity = Gravity.LEFT
            v.chat_Time2.gravity = Gravity.LEFT
        }
    }

    //내가친 채팅 뷰홀더
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //친구목록 모델의 변수들 정의하는부분
        val chat_Text = itemView.chat_Text
        val chat_Time = itemView.chat_Time
    }

    //상대가친 채팅 뷰홀더
    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //친구목록 모델의 변수들 정의하는부분
        val chat_You_Image = itemView.chat_You_Image
        val chat_You_Name = itemView.chat_You_Name
        val chat_Text2 = itemView.chat_Text2
        val chat_Time2 = itemView.chat_Time2
    }

    override fun getItemViewType(position: Int): Int {//여기서 뷰타입을 1, 2로 바꿔서 지정해줘야 내채팅 너채팅을 바꾸면서 쌓을 수 있음

        //내 아이디와 arraylist의 name이 같다면 내꺼 아니면 상대꺼
        return if (arrayList.get(position).name == myID) {
            1
        } else {
            2
        }
    }
}