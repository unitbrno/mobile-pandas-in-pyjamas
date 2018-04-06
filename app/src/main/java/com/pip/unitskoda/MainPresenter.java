package com.pip.unitskoda;

import android.util.Log;

import com.kserno.baseclasses.BasePresenter;
import com.pip.phonexiaapi.ISpeechApi;
import com.pip.phonexiaapi.RealTimeCallback;
import com.pip.phonexiaapi.RecorderCallback;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.Speaker;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.unitskoda.recording.Recorder;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by filipsollar on 6.4.18.
 */

public class MainPresenter extends BasePresenter<MainContract.Screen> implements MainContract.Presenter{

    private ISpeechApi mApi;

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter(ISpeechApi api, Recorder mRecorder, MainContract.Screen screen) {
        super(screen);
        mApi = api;

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        Recorder.INSTANCE.stop();
        mApi.stopProcessing();
    }

    @Override
    public void startListening() {


        final RecorderCallback callback = mApi.getCallback();

        mApi.realTimeProcessing(Recorder.RECORDER_SAMPLERATE, Language.ENGLISH, new RealTimeCallback<SpeechRecognitionResult>() {
            @Override
            public void onStarted() {
                Recorder.INSTANCE.start(new Function1<short[], Unit>() {
                    @Override
                    public Unit invoke(short[] shorts) {
                        callback.onRecording(shorts);
                        return null;
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onSpeakerResult(Speaker speaker) {
                Log.d(TAG, "speaker");
            }

            @Override
            public void onResult(SpeechRecognitionResult result) {
                System.out.println(result.toString());
            }

            @Override
            public void finished() {

            }
        });


    }
}
