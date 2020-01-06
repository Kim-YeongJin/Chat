package com.music961.chat.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.music961.chat.Bean.*
import com.music961.chat.R
import com.music961.yj_prac_1230.recycler_adapter.ChatAdapter
import kotlinx.android.synthetic.main.activity_chat_room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomActivity : AppCompatActivity() {

    var arrayList = arrayListOf<ChatModel>()
    val mAdapter =
        ChatAdapter(this, arrayList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        handlerInit()

        //어댑터 선언
        chat_recyclerview.adapter = mAdapter
        //레이아웃 매니저 선언
        val lm = LinearLayoutManager(this)
        chat_recyclerview.layoutManager = lm
        chat_recyclerview.setHasFixedSize(true)//아이템이 추가삭제될때 크기측면에서 오류 안나게 해줌

        chat_Send_Button.setOnClickListener {
            //아이템 추가 부분
            sendMessage()
        }
    }

    fun sendMessage() {
        val now = System.currentTimeMillis()
        val date = Date(now)
        //나중에 바꿔줄것 밑의 yyyy-MM-dd는 그냥 20xx년 xx월 xx일만 나오게 하는 식
        val sdf = SimpleDateFormat("yyyy-MM-dd")

        val getTime = sdf.format(date)
        var Txt = chating_Text.text.toString()

        //example에는 원래는 이미지 url이 들어가야할 자리
        item = ChatModel(
            myID, Txt, "example", getTime
        )
        mAdapter.addItem(item)
        mAdapter.notifyDataSetChanged()


        CoroutineScope(Dispatchers.Main).launch {
            // 메인 쓰레드 UI 쓰레드 부분
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                //네트워크 쓰레드
                chClient.send(300, myID, youID, Txt)
            }
            // 메인 쓰레드 UI 쓰레드 부분
        }
        //채팅 입력창 초기화
        chating_Text.setText("")
    }

    private fun handlerInit(){
        handlerHouse["chat"] = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg!!)
                when (msg.what) {
                    10->{
                        mAdapter.addItem(item)
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }
}


