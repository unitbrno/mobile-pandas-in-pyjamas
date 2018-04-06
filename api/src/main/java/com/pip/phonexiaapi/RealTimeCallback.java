package com.pip.phonexiaapi;

import com.pip.phonexiaapi.data.Speaker;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface RealTimeCallback<T>{
    void onStarted();
    void onError(Throwable t);
    void onSpeakerResult(Speaker speaker);
    void onResult(T result);
    void finished();


}