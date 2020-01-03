package com.music961.chat.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import com.music961.chat.Bean.chClient
import com.music961.chat.Bean.lo
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

    internal lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //구글 로그인 api
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSigninClient = GoogleSignIn.getClient(this,gso)
        firebaseAuth = FirebaseAuth.getInstance()
        googleLoginBtn.setOnClickListener {
            val signinIntent = googleSigninClient?.signInIntent
            startActivityForResult(signinIntent, RC_SIGN_IN)
        }
        //영진이 형
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
                    Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()
                    firebaseAuth?.currentUser?.let {

                        Log.d("로그", "이름 : ${user?.displayName}")
                        Log.d("로그", "이메일 : ${user?.email}")
                        Log.d("로그", "사진 : ${user?.photoUrl}")
                        Log.d("로그", "이메일 유효성 : ${user?.isEmailVerified}")
                        Log.d("로그", "uid : ${user?.uid}")


                    }

                }
                else{
                    Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
                }
            }
    }
}
