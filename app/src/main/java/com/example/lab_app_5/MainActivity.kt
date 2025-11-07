package com.example.lab_app_5

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private lateinit var txtLatest: TextView
    private lateinit var txtCount: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private var outputStream: OutputStream? = null
    private var sampleCount = 0
    private var isRecording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        txtLatest = findViewById(R.id.txtLatest)
        txtCount = findViewById(R.id.txtCount)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        btnStart.setOnClickListener { startRecording() }
        btnStop.setOnClickListener { stopRecording() }

        if (accelerometer == null) {
            txtLatest.text = "No accelerometer available."
        }
    }

    private fun startRecording() {
        sampleCount = 0
        txtCount.text = "Entries: 0"
        isRecording = true
        createCsvFile()
    }

    private fun stopRecording() {
        isRecording = false
        outputStream?.flush()
        outputStream?.close()
        txtLatest.text = "Stopped. File saved to Downloads.";
    }

    private fun createCsvFile() {
        val resolver = contentResolver
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, "accelerometer_$timeStamp.csv")
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.RELATIVE_PATH, "Download/")
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        outputStream = uri?.let { resolver.openOutputStream(it) }
        outputStream?.write("timestamp,x,y,z\n".toByteArray())
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isRecording) return
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val timestamp = System.currentTimeMillis()
            val line = "$timestamp,$x,$y,$z\n"

            outputStream?.write(line.toByteArray())

            sampleCount++
            txtCount.text = "Entries: $sampleCount"
            txtLatest.text = "Latest: x=%.2f y=%.2f z=%.2f".format(x, y, z)
        }
    }

    // not needed
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}