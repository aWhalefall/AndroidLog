package com.kuaidao.loggview


import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codeview.pluginlogview.LogcatActivity
import com.kuaidao.loggview.databinding.ActivityMainBinding
import com.review.crash.Crashlytics
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var mbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mbinding.root)
        Crashlytics.init(this)
        setonclick()
    }

    private fun setonclick() {
        mbinding.btnE.setOnClickListener {
            Log.e("error", "111111111111111111111111111111111")
        }
        mbinding.btnD.setOnClickListener {
            Log.d("debug", "22222222222222222222222")
        }
        mbinding.window.setOnClickListener {
            LogcatActivity.launchActivity(this)
        }
    }
}