package com.example.hayato.android_kotilin_learn_3

import android.content.Context
import android.hardware.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}


class mySurfaceCallBack :AppCompatActivity(),SensorEventListener ,SurfaceHolder.Callback{
    val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    var surfaceWidth = 0
    var surfaceHeight = 0




    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            Log.d("MainActivity",
                    """
                    |x = ${event.values[0]}
                    |y = ${event.values[1]}
                    |z = ${event.values[2]}
                """.trimMargin()
            )
        }
    }



    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME)
    }

}