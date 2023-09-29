package com.example.type

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.hbb20.CountryCodePicker

class MobileActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile)

        //declaring ccp and edit_text and button
        val ccp : CountryCodePicker = findViewById(R.id.countryCodeHolder)
        val phone : EditText = findViewById(R.id.mobile)
        val confirmbtn :Button = findViewById(R.id.confirm_btn)


        //connecting phone with country code picker
        ccp.registerCarrierNumberEditText(phone)


        //check number is right or wrong
        confirmbtn.setOnClickListener {
            if (!ccp.isValidFullNumber()){
               phone.setError("Phone Number is not Valid")
                return@setOnClickListener
            }
            val intent = Intent (this@MobileActivity,OtpActivity::class.java)
            intent.putExtra("phone",ccp.fullNumberWithPlus)
            startActivity(intent)
        }

    }
}