package com.pip.unitskoda.recording

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.AudioFormat.ENCODING_PCM_16BIT
import android.media.AudioFormat.CHANNEL_IN_MONO
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

object Recorder {

    public const val RECORDER_SAMPLERATE = 8000
    private const val RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO
    private const val RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT
    private const val SAMPLE_SIZE = 2
    private const val BUFFER_SIZE = RECORDER_SAMPLERATE * SAMPLE_SIZE

    private const val POST_RATE_MS = 500L

    var buffer = ShortArray(BUFFER_SIZE)
    val timer = Timer()

    val recorder = AudioRecord(MediaRecorder.AudioSource.MIC,
            RECORDER_SAMPLERATE, RECORDER_CHANNELS,
            RECORDER_AUDIO_ENCODING, BUFFER_SIZE);



    fun start(listener: (ShortArray) -> Unit) {
        recorder.startRecording()

        // Read audio buffer every POST_RATE_MS
        Timer().scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                // Read audio buffer to buffer
                val readSize = recorder.read(buffer, 0, BUFFER_SIZE)
                // Post result with only the size of bytes read to listener
                listener(buffer.sliceArray(0..readSize))
            }
        }, 0, POST_RATE_MS)
    }

    fun stop() {
        timer.cancel()
        recorder.stop()
        recorder.release();
    }

}