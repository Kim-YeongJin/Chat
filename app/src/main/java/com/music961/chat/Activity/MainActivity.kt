package com.music961.chat.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.music961.chat.Bean.chClient
import com.music961.chat.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    internal lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoroutineScope(Dispatchers.Main).launch {
            // 메인 쓰레드 UI 쓰레드 부분
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                //네트워크 쓰레드
                chClient.start()
            }
            // 메인 쓰레드 UI 쓰레드 부분
        }

        preferences = getSharedPreferences("USERSIGN", Context.MODE_PRIVATE)
        val editor = preferences.edit()


        //버튼을 클릭하면 입력한 이름을 쉐어드프리퍼런스에 내이름을 저장한다.
        //또한 그 이름을 가지고 채팅방으로 이동한다.
        enter.setOnClickListener{
            editor.putString("name", yourId.text.toString())
            val intent = Intent(this, ChatRoomActivity::class.java)
            startActivity(intent)
        }

    }
}
