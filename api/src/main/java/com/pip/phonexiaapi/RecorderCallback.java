package com.pip.phonexiaapi;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface RecorderCallback {
    void onRecording(short[] data);
    void finished();
}