package com.example.androidcse227.Unit4

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcse227.R
import kotlin.math.round

// NOTE: implement SensorEventListener here
class Sensors : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager

    // motion
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null

    // position
    private var rotationVector: Sensor? = null

    // environment
    private var temperatureSensor: Sensor? = null
    private var lightSensor: Sensor? = null
    private var pressureSensor: Sensor? = null
    private var humiditySensor: Sensor? = null

    // UI
    private lateinit var tvAccel: TextView
    private lateinit var tvGyro: TextView
    private lateinit var tvPos: TextView
    private lateinit var tvTemp: TextView
    private lateinit var tvLight: TextView
    private lateinit var tvPressure: TextView
    private lateinit var tvHumidity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_sensors)

        // find views
        tvAccel = findViewById(R.id.tvAccelerometer)
        tvGyro = findViewById(R.id.tvGyroscope)
        tvPos = findViewById(R.id.tvPosition)
        tvTemp = findViewById(R.id.tvTemperature)
        tvLight = findViewById(R.id.tvLight)
        tvPressure = findViewById(R.id.tvPressure)
        tvHumidity = findViewById(R.id.tvHumidity)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // get sensors (may be null on some devices)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        rotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
    }

    override fun onResume() {
        super.onResume()
        val rate = SensorManager.SENSOR_DELAY_UI
        accelerometer?.also { sensorManager.registerListener(this, it, rate) }
        gyroscope?.also { sensorManager.registerListener(this, it, rate) }
        rotationVector?.also { sensorManager.registerListener(this, it, rate) }
        temperatureSensor?.also { sensorManager.registerListener(this, it, rate) }
        lightSensor?.also { sensorManager.registerListener(this, it, rate) }
        pressureSensor?.also { sensorManager.registerListener(this, it, rate) }
        humiditySensor?.also { sensorManager.registerListener(this, it, rate) }
    }

    override fun onPause() {
        super.onPause()
        // 'this' is now a SensorEventListener, so this call is valid
        sensorManager.unregisterListener(this)
    }

    // small helper to make floats readable
    private fun f(v: Float) = (round(v * 100) / 100).toString()

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                val x = f(event.values[0]); val y = f(event.values[1]); val z = f(event.values[2])
                tvAccel.text = "Accel (m/s²): x=$x  y=$y  z=$z"
            }
            Sensor.TYPE_GYROSCOPE -> {
                val x = f(event.values[0]); val y = f(event.values[1]); val z = f(event.values[2])
                tvGyro.text = "Gyro (rad/s): x=$x  y=$y  z=$z"
            }
            Sensor.TYPE_ROTATION_VECTOR -> {
                val rot = FloatArray(9)
                val orient = FloatArray(3)
                SensorManager.getRotationMatrixFromVector(rot, event.values)
                SensorManager.getOrientation(rot, orient)
                val az = f(orient[0]); val pitch = f(orient[1]); val roll = f(orient[2])
                tvPos.text = "Azimuth=$az  Pitch=$pitch  Roll=$roll"
            }
            Sensor.TYPE_AMBIENT_TEMPERATURE -> {
                tvTemp.text = "Temp: ${f(event.values[0])} °C"
            }
            Sensor.TYPE_LIGHT -> {
                tvLight.text = "Light: ${f(event.values[0])} lx"
            }
            Sensor.TYPE_PRESSURE -> {
                tvPressure.text = "Pressure: ${f(event.values[0])} hPa"
            }
            Sensor.TYPE_RELATIVE_HUMIDITY -> {
                tvHumidity.text = "Humidity: ${f(event.values[0])} %"
            }
        }
    }

    // implement this since we declared SensorEventListener
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // not used for minimal example
    }
}
