package com.example.testebackground

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.app.Activity
import android.util.Log

class MyService : Service() {
    private var startTime: Long = 0
    private var pauseTime: Long = 0
    private var millis: Long = 0
    private val binder: IBinder = LocalBinder()

    private lateinit var activity: Callbacks

    private val handler = Handler()
    private var serviceRunnable: Runnable = object : Runnable {
        override fun run() {
            millis = if (startTime != 0.toLong()){
                System.currentTimeMillis() - startTime
            }else{
                pauseTime - startTime
            }

            activity.updateClient(millis)
            handler.postDelayed(this, 1000)
            Log.d("MyService: ", "Executando o serviço...")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    fun registerClient(activity: Activity) {
        this.activity = activity as Callbacks
    }

    fun startCounter() {
        startTime = System.currentTimeMillis()
        handler.postDelayed(serviceRunnable, 0)
    }

    fun stopCounter() {
        startTime = 0
        pauseTime = 0
        activity.updateClient(0)
        handler.removeCallbacks(serviceRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //callbacks interface for communication with service clients!
    interface Callbacks {
        fun updateClient(millis: Long)
    }

    inner class LocalBinder : Binder() {
        val serviceInstance: MyService
            get() = this@MyService
    }
}