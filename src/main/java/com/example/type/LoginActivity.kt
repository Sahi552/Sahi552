package com.example.type

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {


    //declaring All necessary variables and its type
    lateinit var user: EditText
    lateinit var btn2: Button
    var phn2: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //assigning value to variable by its id
        user = findViewById(R.id.user)
        btn2 = findViewById(R.id.login_btn)
        phn2 = intent.extras?.getString("phone")


        getusername()

    }

    private fun getusername() {
        currentUserDetails()?.get()?.addOnCompleteListener(OnCompleteListener<DocumentSnapshot>(){

            //statements
        })
    }

    fun currentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun currentUserDetails(): DocumentReference? {
        val currentUserId = currentUserId()
        return if (currentUserId != null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUserId)
        } else {
            null
        }
    }
}