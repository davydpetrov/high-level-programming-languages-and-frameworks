package com.example.hlplf_pw3

import android.os.Bundle
import android.widget.TextView
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val textView = TextView(this)
        textView.text = "Hello World"
        textView.textSize = 24f
        textView.gravity = Gravity.CENTER

        setContentView(textView)
    }
}