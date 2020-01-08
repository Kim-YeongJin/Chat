package com.music961.chat.Network

import GodHand
import MacheteSecurity
import MctSecRSA_CL
import android.util.Log
import com.music961.chat.Bean.*
import java.security.PublicKey
import java.text.SimpleDateFormat
import java.util.*


class ChatClient : GodHand("192.168.1.108" , 3000 , 10240, 10240) {


    override fun appEnd() {
    }

    override fun sopoOpen(protocol: Int, sopo: Array<Any>) {

        when(protocol) {
            // 서버로부터 공개키 수신
            11 -> {
                val temp = sopo[0] as String

                Log.d("로그","퍼블릭 키 $temp")
                //sv_publkey = temp
                cl_rsa = MctSecRSA_CL(sv_publkey, Charsets.ISO_8859_1)

                toWhat("login", 10)
            }
            // 채팅방으로 이동 (채팅 요청 받음)
            401 -> {
                val yourID = sopo[0] as String

                youID = yourID

                toWhat("chatroom", 20)

                send(501, yourID) // 요청한 유저에게 채팅 수락 전송
                toWhat("chatroom", 21)

            }
            // 채팅 내용 수신
            301 -> {
                val yourID = sopo[0] as String
                val chatlog = sopo[1] as String

                val now = System.currentTimeMillis()
                val date = Date(now)

                val sdf = SimpleDateFormat("yyyy-MM-dd")

                val getTime = sdf.format(date)

                youID = yourID

                item = ChatModel(
                    youID, chatlog, "example", getTime
                )

                toWhat("chat", 10)

            }
            // 단톡방 입장
            601 -> {
                val userID = sopo[0] as String

                item = ChatModel("", userID + "님이 입장하셨습니다", "", "")

                toWhat("chat", 10)
            }
            // 단톡방 수신
            604-> {
                val userID = sopo[0] as String
                val chatlog = sopo[1] as String

                item = ChatModel(userID, chatlog, "", "")

                toWhat("chat", 10)
            }

        }
    }

    override fun sopoProtocol(protocol: Int) {
        when(protocol) {
            //회원가입 승인
            101 -> {
                toWhat("login", 11)
            }
            // 채팅방 생성
            500 -> {
                toWhat("chatroom", 20)
            }
        }
    }
}

