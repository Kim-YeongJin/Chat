package com.music961.chat.Bean

import MctSecRSA_CL
import android.os.Handler
import android.util.Log
import com.music961.chat.Network.ChatClient
import java.security.PublicKey

val handlerHouse = HashMap<String, Handler>()


val chClient = ChatClient()

lateinit var sv_publkey : PublicKey

lateinit var cl_rsa : MctSecRSA_CL

var youID = ""
var myID = ""
var item = ChatModel(
    "", "", "example", ""
)
var room = ChatRoomInfo (
    0, ""
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

fun print(str: Any){
    System.out.println(str)
}