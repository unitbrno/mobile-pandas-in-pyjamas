package com.pip.phonexiaapi;

import android.telecom.Call;

import com.pip.phonexiaapi.data.AudioFileInfoResult;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;

import java.io.File;

import rx.Observable;
import rx.Single;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface ISpeechApi {

     void realTimeProcessing(
        int frequency,
        Language language,
        RealTimeCallback<SpeechRecognitionResult> realTimeCallback
    );

    Single speechToText();

    RecorderCallback getCallback();

    void startSpeakerIdentification(
            String groupName
    );

    void createSpeakerModel(String userName, File wavFile, ApiCallback<AudioFileInfoResult> callback);

}
