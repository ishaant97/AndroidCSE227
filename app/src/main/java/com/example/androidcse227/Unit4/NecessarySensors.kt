package com.example.androidcse227.Unit4

import android.hardware.*
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcse227.R

class NecessarySensors : AppCompatActivity(), SensorEventListener {

    lateinit var sm: SensorManager

    // Create 3 variables for 3 separate text views
    lateinit var tvMotion: TextView
    lateinit var tvPos: TextView
    lateinit var tvEnv: TextView

    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        setContentView(R.layout.activity_necessary_sensors)

        // 1. Link them to the XML IDs
        tvMotion = findViewById(R.id.tvMotion)
        tvPos = findViewById(R.id.tvPosition)
        tvEnv = findViewById(R.id.tvEnvironment)

        sm = getSystemService(SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        // 2. Register all three
        val accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val prox = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        val light = sm.getDefaultSensor(Sensor.TYPE_LIGHT)

        // Using safe calls (?) in case a sensor is missing on the device
        accel?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        prox?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        light?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
    }

    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        // 3. Update the specific TextView based on the sensor type
        when (event.sensor.type) {

            Sensor.TYPE_ACCELEROMETER -> {
                val x = event.values[0]
                val y = event.values[1]
                // Overwrite the text (don't append)
                tvMotion.text = "Motion Sensor\nX: $x\nY: $y"
            }

            Sensor.TYPE_PROXIMITY -> {
                val dist = event.values[0]
                tvPos.text = "Position Sensor\nDistance: $dist cm"
            }

            Sensor.TYPE_LIGHT -> {
                val lux = event.values[0]
                tvEnv.text = "Environment Sensor\nLight: $lux lx"
            }
        }
    }

    override fun onAccuracyChanged(s: Sensor?, i: Int) {}
}