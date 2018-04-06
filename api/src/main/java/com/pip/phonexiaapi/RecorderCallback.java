package com.pip.phonexiaapi;

/**
 * Created by filipsollar on 6.4.18.
 */

interface RecorderCallback {
    void onRecording(Short[] data);
    void finished();
}