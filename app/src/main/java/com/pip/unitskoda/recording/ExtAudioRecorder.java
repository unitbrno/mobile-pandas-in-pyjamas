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


public class ExtAudioRecorder {
    private File file;
    private Context mContext;

    private WavAudioRecorder mRecorder;

    @Inject
    public ExtAudioRecorder(Context context) {
        mContext = context;
    }


    public void startRecording(String fileName) {

        file = new File(mContext.getFilesDir(), fileName + ".wav");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mRecorder = WavAudioRecorder.getInstance();
        mRecorder.setOutputFile(file.getAbsolutePath());
        mRecorder.prepare();
        mRecorder.start();
    }

    public File stop(final Callback fileCallback) {
        release();

        fileCallback.onSuccess(file);

        return file;
    }


    public void release() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder = null;
        }

    }
    public interface Callback {
        void onSuccess(File file);
        void onFail(Throwable t);
    }
}