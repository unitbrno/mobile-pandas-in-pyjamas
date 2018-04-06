package com.pip.unitskoda.recording

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.AudioFormat.ENCODING_PCM_16BIT
import android.media.AudioFormat.CHANNEL_IN_MONO
import io.reactivex.Observable
import java.util.*


class WavRecorder(filename: String) {

    val RECORDER_SAMPLERATE = 8000
    private val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
    private val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT

    private val recorder = ExtAudioRecorder(true, MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLERATE, RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING);

    init {
        recorder.setOutputFile(filename)
    }

    fun start() {
        recorder.prepare()
        recorder.start()
    }

    fun stop() {
        recorder.stop()
        recorder.release();
    }

}