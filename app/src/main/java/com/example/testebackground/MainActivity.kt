package com.example.testebackground

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), MyService.Callbacks {
    private lateinit var btnStates: Button
    private lateinit var hour: TextView
    private lateinit var minute: TextView
    private lateinit var second: TextView
    private lateinit var myService: MyService
    private lateinit var serviceIntent: Intent
    private lateinit var myConnection: ServiceConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        initService()

        initListeners()

    }

    private fun initViews() {
        btnStates = findViewById(R.id.btnStates)
        hour = findViewById(R.id.hh)
        minute = findViewById(R.id.MM)
        second = findViewById(R.id.ss)
    }

    private fun initService() {
        serviceIntent = Intent(this@MainActivity, MyService::class.java)

        myConnection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
                val binder = service as MyService.LocalBinder
                myService = binder.serviceInstance
                myService.registerClient(this@MainActivity)
                Log.d("MainActivity: ", "Service started...")
            }

            override fun onServiceDisconnected(className: ComponentName?) {
                Log.d("MainActivity: ", "Service stopped...")
            }
        }

        startService(serviceIntent)
        bindService(serviceIntent, myConnection, Context.BIND_AUTO_CREATE)
    }

    private fun initListeners() {
        btnStates.setOnClickListener {
            when(btnStates.text){
                "Start" -> {
                    myService.startCounter()
                    btnStates.text = "Stop"
                }
                "Stop" -> {
                    myService.stopCounter()
                    btnStates.text = "Start"
                }
            }
        }
    }

    override fun updateClient(millis: Long) {
        val decimalFormat = DecimalFormat("00")
        second.text = (decimalFormat.format((millis / 1000) % 60)).toString()
        minute.text = decimalFormat.format(((millis / (1000*60)) % 60)).toString()
        hour.text   = decimalFormat.format(((millis / (1000*60*60)) % 24)).toString()
    }
    enum class CounterStates{
        START,
        PAUSE,
        RESUME
    }
}
