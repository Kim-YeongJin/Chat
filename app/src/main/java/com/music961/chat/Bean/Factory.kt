package com.music961.chat.Bean

import android.os.Handler
import android.util.Log
import com.music961.chat.Network.ChatClient
import java.net.SocketAddress

val handlerHouse = HashMap<String, Handler>()
val ChatRoom = HashMap<String, SocketAddress>()


val chClient = ChatClient()

var youID = ""
var myID = ""
var item = ChatModel(
    "", "", "example", ""
)

fun toWhat(str: String, arg: Int) {
    try {
        val h = handlerHouse.getValue(str)
        h.sendMessage(h.obtainMessage().apply { what = arg })
    } catch (e: NoSuchElementException) {
    }  //핸들러 없으면 무시
}

fun toWhatObj(str: String, arg: Int, ob: Any) {
    try {
        val h = handlerHouse.getValue(str)
        h.sendMessage(h.obtainMessage().apply { what = arg; obj = ob }) //arg1 -> what 으로 변경할 경우 문제 발생함
    } catch (e: NoSuchElementException) { }
}

fun lo(str:String){
    Log.d("로그",str)
}