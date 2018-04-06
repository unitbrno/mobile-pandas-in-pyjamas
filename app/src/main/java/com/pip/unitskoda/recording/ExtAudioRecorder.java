package com.pip.unitskoda.recording;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentValues;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;

import javax.inject.Inject;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;

public class ExtAudioRecorder {
    private File file;
    private MediaRecorder recorder;
    private Context mContext;

    @Inject
    public ExtAudioRecorder(Context context) {
        mContext = context;
    }



    public void startRecording(String fileName) {

        file = new File(Environment.getExternalStorageDirectory(), fileName + ".mp4");

        recorder = new MediaRecorder();

        ContentValues values = new ContentValues(3);

        values.put(MediaStore.MediaColumns.TITLE, fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(file.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
    }

    public File stop(final Callback fileCallback) {
        recorder.stop();
        recorder.release();

        recorder = null;

        IConvertCallback callback = new IConvertCallback() {
            @Override
            public void onSuccess(File file) {
                fileCallback.onSuccess(file);
            }

            @Override
            public void onFailure(Exception e) {
                fileCallback.onFail(e);
            }
        };
        AndroidAudioConverter.with(mContext)
                .setFile(file)
                .setFormat(cafe.adriel.androidaudioconverter.model.AudioFormat.WAV)
                .setCallback(callback)
                .convert();

        return file;
    }


    public void release() {
        if (recorder != null) {
            recorder.release();
            recorder.stop();
            recorder = null;
        }

    }
    public interface Callback {
        void onSuccess(File file);
        void onFail(Throwable t);
    }
}