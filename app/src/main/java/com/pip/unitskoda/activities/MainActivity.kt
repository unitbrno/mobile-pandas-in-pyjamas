package com.pip.unitskoda.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.pip.unitskoda.R
import com.pip.unitskoda.recording.Recorder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Recorder.start {
            Log.d("TAG", it.toString())
        }
    }
}
