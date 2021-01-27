package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer: CountDownTimer
    lateinit var startButton: Button
    lateinit var coutdownDisplay: TextView
    lateinit var startButton2: Button
    lateinit var startButton3: Button
    lateinit var startButton4: Button
    lateinit var startButton5: Button
    var isrunning = false

    var timeToCountDownInMs = 10000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener() {
            if (!isrunning) {
                isrunning = true
                startButton.text = "Stop"
                startCountDown(it)
            } else {
                startButton.text = "Start"
                timer.cancel()
                isrunning = false
            }
        }

        startButton2 = findViewById<Button>(R.id.startcountdownsixty)
        startButton2.setOnClickListener() {
            timeToCountDownInMs = 3600000L
        }
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        startButton3 = findViewById<Button>(R.id.startcountdownthirty)
        startButton3.setOnClickListener() {
            timeToCountDownInMs = 1800000L
        }

        startButton4 = findViewById<Button>(R.id.startcountdownninty)
        startButton4.setOnClickListener() {
            timeToCountDownInMs = 5400000L
        }
        startButton5 = findViewById<Button>(R.id.startcountdownonetwenty)
        startButton5.setOnClickListener() {
            timeToCountDownInMs = 7200000L
        }
    }

    fun startCountDown(v: View) {

        timer = object : CountDownTimer(timeToCountDownInMs, timeTicks) {
            override fun onFinish() {
                isrunning = false
                Toast.makeText(this@MainActivity, "Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
    }

    fun updateCountDownDisplay(timeInMs: Long) {
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }
}