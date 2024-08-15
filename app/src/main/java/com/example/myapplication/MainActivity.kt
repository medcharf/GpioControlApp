package com.example.gpiocontrol

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Execute the Termux commands to control GPIO
        executeTermuxCommands()
    }

    private fun executeTermuxCommands() {
        Thread {
            try {
                // Run Termux commands to control GPIO
                val process = Runtime.getRuntime().exec("su")
                val osw = OutputStreamWriter(process.outputStream)
                osw.write("echo 26 > /sys/class/gpio/export\n")
                osw.write("echo out > /sys/class/gpio/gpio26/direction\n")
                osw.write("echo 0 > /sys/class/gpio/gpio26/value\n")
                osw.flush()
                osw.close()
                process.waitFor()

                // Show a message to confirm
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this, "GPIO 26 set to HIGH", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(this, "Failed to set GPIO 26", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}
