package com.pip.phonexiaapi;

import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;

import rx.Observable;
import rx.Single;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface ISpeechApi {

    RecorderCallback realTimeProcessing(
        int frequency,
        Language language,
        RealTimeCallback<SpeechRecognitionResult> realTimeCallback
    );

    Single speechToText();

}
