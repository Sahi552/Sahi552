package com.example.type

 import android.content.Intent
 import android.os.Bundle
 import android.widget.Button
 import android.widget.EditText
 import android.widget.TextView
 import android.widget.Toast
 import androidx.appcompat.app.AppCompatActivity
 import com.google.firebase.FirebaseException
 import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.auth.PhoneAuthCredential
 import com.google.firebase.auth.PhoneAuthOptions
 import com.google.firebase.auth.PhoneAuthProvider
 import java.util.Timer
 import java.util.TimerTask
 import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    //decalring variable and its type
    lateinit var otpnum : EditText
    lateinit var btn : Button
    lateinit var res : TextView
    var phn : String? =""
    private lateinit var mauth: FirebaseAuth
    lateinit var verifycode : String
    lateinit var token : PhoneAuthProvider.ForceResendingToken
    var isresend : Boolean = false
    var timeoutsec = 60L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        //assigning value to variable by its id
        otpnum = findViewById(R.id.otp)
        btn = findViewById(R.id.next_btn)
        res = findViewById(R.id.resend)
        mauth = FirebaseAuth.getInstance()
        phn = intent.extras?.getString("phone")

        //just appear and vanish
        Toast.makeText(this,phn,Toast.LENGTH_SHORT).show()

        //function for otp verify
        sendOtp(phn,false)

        btn.setOnClickListener {
            val EnterOtp : String = otpnum.text.toString()
            val credential = PhoneAuthProvider.getCredential(verifycode,EnterOtp)
            signIn(credential)
        }

        //function for otp resend
        res.setOnClickListener {
            sendOtp(phn,true)
        }
    }

    private fun sendOtp(phone: String?, isresend: Boolean) {
        resendOtptimer() //function for otptimer
        val mcallback = object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onVerificationCompleted(phnauthcredit: PhoneAuthCredential) {
                signIn(phnauthcredit)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(applicationContext,"Otp Verification failed",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                verifycode = p0
                token = p1
                Toast.makeText(applicationContext,"Otp sent successfully",Toast.LENGTH_SHORT).show()
            }


        }

        //authentication
        val build = PhoneAuthOptions.newBuilder(mauth)
            .setPhoneNumber(phone!!)
            .setTimeout(timeoutsec,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mcallback)

        if (isresend){
            PhoneAuthProvider.verifyPhoneNumber(build.setForceResendingToken(token).build())
        }
        else{
            PhoneAuthProvider.verifyPhoneNumber(build.build())
        }
    }


    private fun signIn(credential: PhoneAuthCredential) {
        // Inside this method, we are checking if
        // the code entered is correct or not.
        mauth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If the code is correct and the task is successful,
                    // we are sending our user to a new activity.
                    val intent = Intent(this@OtpActivity, LoginActivity::class.java)
                    intent.putExtra("Phone",phn)
                    startActivity(intent)
                    finish()
                } else {
                    // If the code is not correct, then we are
                    // displaying an error message to the user.
                    Toast.makeText(this@OtpActivity,"Otp verification Failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun resendOtptimer() {
        res.isEnabled = false
        val timer = Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                timeoutsec--
                if (timeoutsec <= 0) {
                    timeoutsec = 60
                    timer.cancel()
                    runOnUiThread() {
                        res.isEnabled = true
                        res.text = "Resend OTP" // Update the text to indicate the option to resend
                    }
                }

            }
        },0,1000)
    }



}


