package com.rajnet.ondrej.firebase

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var referance: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var sensorManager: SensorManager? = null

    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        database = FirebaseDatabase.getInstance()
        referance = database.getReference("Steps")

        btn_send.setOnClickListener{
            sendData()
            Toast.makeText(this, "Tvůj pokus uložen!", Toast.LENGTH_SHORT).show()

        }
        btn_getData.setOnClickListener{
            startActivity(Intent(applicationContext, GetData::class.java))
        }

        resetStep()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this, "Zařízení nemá pohybový senzor", Toast.LENGTH_SHORT).show()
        }else{
            sensorManager?.registerListener(this, stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            tv_stepsTaken.text = ("$currentSteps")
            }

        }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }


    private fun resetStep(){
        tv_stepsTaken.setOnClickListener{
            Toast.makeText(this, "Dlouhé podržení pro reset kroků", Toast.LENGTH_SHORT).show()
        }
        tv_stepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            tv_stepsTaken.text = 0.toString()
            true
        }
    }


    private fun sendData(){
        var name = et_name.text.toString().trim()
        var date = et_date.text.toString().trim()

        if(name.isNotEmpty() && date.isNotEmpty()){
            var model = DatabaseModel(name, date, totalSteps.toInt())
            var id = referance.push().key

            referance.child(id!!).setValue(model)
            et_name.setText("")
            et_date.setText("")

        }else{
            Toast.makeText(applicationContext,"Vše musí být vyplněno", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miLogout) {
            Log.i(TAG, "Logout")
            auth.signOut()
            val logouIntent = Intent(this, Login::class.java)
            logouIntent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logouIntent )
        }
        return super.onOptionsItemSelected(item)
    }

}