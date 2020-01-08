package com.music961.chat.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.music961.chat.Bean.*
import com.music961.chat.R
import com.music961.chat.recycler_adapter.ChatListAdapter
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListActivity : AppCompatActivity() {

    var arrayList = arrayListOf<ChatRoomInfo>()
    val chatListadapter = ChatListAdapter(this, arrayList)
    var isIn:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        handlerInit()

        room = ChatRoomInfo(0, "채팅방")

        arrayList.add(room)

        enter.setOnClickListener {
            youID = yourId.text.toString()
            room = ChatRoomInfo(arrayList.size, youID)

            for(i in 0 until arrayList.size) {
                if (arrayList[i].yourID == youID) {
                    Toast.makeText(this@ChatListActivity, "이미 존재하는 대화방", Toast.LENGTH_SHORT).show()
                    isIn = true
                    break
                }
                else {
                    isIn = false
                }
            }

            if(arrayList.size == 1 || !isIn) {
                chatListadapter.addItem(room)
                chatListadapter.notifyDataSetChanged()

                CoroutineScope(Dispatchers.Main).launch {
                    // 메인 쓰레드 UI 쓰레드 부분
                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                        //네트워크 쓰레드
                        chClient.send(400, myID, youID)
                    }
                    // 메인 쓰레드 UI 쓰레드 부분
                }

                isIn = false
            }
            yourId.setText("")
        }

        //채팅 리스트 리사이클러
        chatting_list_recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = chatListadapter
        }
        //로그아웃 버튼
        logout_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

    private fun handlerInit(){
        handlerHouse["chatroom"] = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg!!)
                when (msg.what) {
                    20->{
                        val main = Intent(this@ChatListActivity, ChatRoomActivity::class.java)
                        startActivity(main)
                        // 채팅방 생성
                    }

                    21-> {
                        room = ChatRoomInfo(arrayList.size, youID)
                        for (i in 0 until arrayList.size) {
                            if (arrayList[i].yourID == youID) {
                                Toast.makeText(
                                    this@ChatListActivity,
                                    "이미 존재하는 대화방",
                                    Toast.LENGTH_SHORT
                                ).show()
                                isIn = true
                                break
                            } else {
                                isIn = false
                            }
                        }

                        if (arrayList.size == 0 || !isIn) {
                            chatListadapter.addItem(room)
                            chatListadapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }
}
