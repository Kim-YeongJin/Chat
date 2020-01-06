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
import com.google.firebase.auth.FirebaseAuth
import com.music961.chat.Bean.chClient
import com.music961.chat.Bean.handlerHouse
import com.music961.chat.Bean.myID
import com.music961.chat.Bean.youID
import com.music961.chat.R
import com.music961.chat.recycler_adapter.ChatListAdapter
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        handlerInit()

        //버튼을 클릭하면 입력한 이름을 쉐어드프리퍼런스에 내이름을 저장한다.
        //또한 그 이름을 가지고 채팅방으로 이동한다.
        enter.setOnClickListener {
            youID = yourId.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                // 메인 쓰레드 UI 쓰레드 부분
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    //네트워크 쓰레드
                    chClient.send(400, myID, youID)
                }
                // 메인 쓰레드 UI 쓰레드 부분
            }
        }

        //채팅 리스트 리사이클러
        chatting_list_recycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
            adapter = ChatListAdapter()
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

                    21->{
                        Toast.makeText(applicationContext,"거부", Toast.LENGTH_SHORT).show()
                        // 채팅 거부
                    }
                }
            }
        }
    }
}
