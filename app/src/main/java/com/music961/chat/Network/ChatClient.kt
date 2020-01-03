package com.music961.chat.Network

import GodHand

class ChatClient : GodHand("192.168.1.105" , 3000 , 10240, 10240) {
    override fun appEnd() {
    }

    override fun sopoOpen(protocol: Int, sopo: Array<Any>) {
        when(protocol) {

        }
    }

    override fun sopoProtocol(protocol: Int) {
        when(protocol) {
            // 로그인 승인
            200 -> {

            }
            // 로그인 실패
            201 -> {

            }
            //회원가입 승인
            202 -> {

            }
            // 아이디 중복
            203 -> {
                
            }
        }
    }
}