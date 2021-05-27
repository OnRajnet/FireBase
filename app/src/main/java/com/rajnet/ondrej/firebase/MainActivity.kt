package com.rajnet.ondrej.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        referance = database.getReference("Users")

        btn_send.setOnClickListener{
            sendData()
        }
        btn_getData.setOnClickListener{
            startActivity(Intent(applicationContext, GetData::class.java))
        }

    }

    private fun sendData(){
        var name = et_name.text.toString().trim()
        var email = et_email.text.toString().trim()

        if(name.isNotEmpty() && email.isNotEmpty()){
            var model = DatabaseModel(name, email)
            var id = referance.push().key

            referance.child(id!!).setValue(model)
            et_name.setText("")
            et_email.setText("")

        }else{
            Toast.makeText(applicationContext,"Vše musí být vyplněno", Toast.LENGTH_LONG).show()
        }
    }

}