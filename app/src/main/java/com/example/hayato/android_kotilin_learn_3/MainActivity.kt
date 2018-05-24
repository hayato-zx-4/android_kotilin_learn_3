package com.example.hayato.android_kotilin_learn_3

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Paint
import android.hardware.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        val holder = surfaceView.holder
        holder.addCallback(mySurfaceCallBack(this))

    }

}


class mySurfaceCallBack(activity: AppCompatActivity) : SensorEventListener, SurfaceHolder.Callback {
    private val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private val activity = activity
    private var surfaceWidth = 0
    private var surfaceHeight = 0
    private val radius = 50.0f
    private val coef = 1000.0f

    private var ballX = 0f
    private var ballY = 0f
    private var vx = 0f
    private var vy = 0f
    private var time = 0L


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        event ?: return
        if (time == 0L) System.currentTimeMillis()
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val x = -event.values[0]
            val y = event.values[1]

            var t = (System.currentTimeMillis() - time).toFloat()

            time = System.currentTimeMillis()
            t /= 1000.0f

            val dx = vx * t + x * t * t / 2.0f
            val dy = vy * t + y * t * t / 2.0f
            ballX += dx * coef
            ballY += dy * coef
            vx += x * t
            vy += y * t

            if (ballX - radius < 0 && vy < 0) {
                vx = -vx / 1.5f
                ballX = radius
            } else if (ballX + radius > surfaceWidth && vx > 0) {
                vx = -vx / 1.5f
                ballX = surfaceWidth - radius
            }

            if (ballY - radius < 0 && vy < 0) {
                vy = -vy / 1.5f
                ballY = radius
            } else if (ballY + radius > surfaceHeight && vy > 0) {
                vy = -vy / 1.5f
                ballY = surfaceHeight - radius
            }

            drawCanvas()


        }


    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        surfaceWidth = width
        surfaceHeight = height
        ballX = (width / 2).toFloat()
        ballY = (height / 2).toFloat()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        sensorManager.unregisterListener(this)

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_GAME)
    }

    private fun drawCanvas() {
        val canvas = activity.surfaceView.holder.lockCanvas().apply {
            drawColor(Color.BLUE)
            drawCircle(ballX, ballY, radius, Paint().apply { color = Color.WHITE })
        }
        activity.surfaceView.holder.unlockCanvasAndPost(canvas)
    }
}


