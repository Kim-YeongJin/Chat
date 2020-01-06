package com.music961.chat.Network

import GodHand
import android.content.SharedPreferences
import com.music961.chat.Bean.*
import java.net.SocketAddress
import java.text.SimpleDateFormat
import java.util.*

class ChatClient : GodHand("192.168.1.105" , 3000 , 10240, 10240) {

    internal lateinit var preferences: SharedPreferences

    override fun appEnd() {
    }

    override fun sopoOpen(protocol: Int, sopo: Array<Any>) {
        when(protocol) {
            401 -> {
                val yourID = sopo[0] as String
                val yourIP = sopo[1] as SocketAddress

                youID = yourID

                toWhat("chatroom", 20)

                send(501, yourID)
            }

            301 -> {
                val yourID = sopo[0] as String
                val chatlog = sopo[1] as String

                val now = System.currentTimeMillis()
                val date = Date(now)
                //나중에 바꿔줄것 밑의 yyyy-MM-dd는 그냥 20xx년 xx월 xx일만 나오게 하는 식
                val sdf = SimpleDateFormat("yyyy-MM-dd")

                val getTime = sdf.format(date)

                youID = yourID

                item = ChatModel(
                    youID, chatlog, "example", getTime
                )

                toWhat("chat", 10)

            }

        }
    }

    override fun sopoProtocol(protocol: Int) {
        when(protocol) {
            //회원가입 승인
            202 -> {

            }
            // 아이디 중복
            203 -> {
                
            }
            // 채팅방 생성
            500 -> {
                toWhat("chatroom", 20)
            }
        }
    }
}

