package com.music961.chat.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.music961.chat.Bean.*
import com.music961.chat.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    //구글 api 변수 선언
    private val RC_SIGN_IN = 9001
    private var googleSigninClient: GoogleSignInClient? = null
    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handlerInit()

        //구글 로그인 api
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSigninClient = GoogleSignIn.getClient(this,gso)
        firebaseAuth = FirebaseAuth.getInstance()
        googleLoginBtn.setOnClickListener {
            val signinIntent = googleSigninClient?.signInIntent
            startActivityForResult(signinIntent, RC_SIGN_IN)

        }

        CoroutineScope(Dispatchers.Main).launch {
            // 메인 쓰레드 UI 쓰레드 부분
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                //네트워크 쓰레드
                chClient.start()
            }
            // 메인 쓰레드 UI 쓰레드 부분
        }
    }

    //구글 로그인 api
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
            }catch(e: ApiException){

            }
        }
    }

    //구글 로그인 api
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(acct.idToken,null)
        firebaseAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    val user = firebaseAuth?.currentUser
                    lo("이메일 ${firebaseAuth?.currentUser?.email}")
                    myID = "${user?.email}"

                    CoroutineScope(Dispatchers.Main).launch {
                        // 메인 쓰레드 UI 쓰레드 부분
                        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                            //네트워크 쓰레드
                            chClient.send(10)
                        }
                        // 메인 쓰레드 UI 쓰레드 부분
                    }
                }
                else{
                    Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
                    lo("로그인 실패")
                }
            }
    }

    private fun handlerInit(){
        handlerHouse["login"] = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg!!)
                when (msg.what) {
                    10->{
                        CoroutineScope(Dispatchers.Main).launch {
                            // 메인 쓰레드 UI 쓰레드 부분
                            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                                //네트워크 쓰레드
                                Log.d("로그","보내는 키 ${cl_rsa.enc(myID)}")
                                chClient.send(100, cl_rsa.enc(myID))
                            }
                            // 메인 쓰레드 UI 쓰레드 부분
                        }
                    }
                    11 -> {
                        val temp = Intent(this@MainActivity, ChatListActivity::class.java)
                        startActivity(temp)
                    }

                }
            }
        }
    }
}
