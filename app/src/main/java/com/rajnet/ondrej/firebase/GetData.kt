package com.rajnet.ondrej.firebase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_get_data.*

class GetData : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_data)

        database = FirebaseDatabase.getInstance()
        referance = database.getReference("Users")

        getData()
    }

    private fun getData(){
        referance.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var list = ArrayList<DatabaseModel>()
                for(data in snapshot.children){
                    var model = data.getValue(DatabaseModel::class.java)
                    list.add(model as DatabaseModel)
                }

                if(list.size > 0){
                    val adapter = DataAdapter(list)
                    recyclerview.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel", error.toString() )
            }


        })
    }
}