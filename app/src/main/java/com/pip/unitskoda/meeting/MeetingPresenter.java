package com.pip.unitskoda.meeting;

import android.util.Log;

import com.kserno.baseclasses.BasePresenter;
import com.pip.phonexiaapi.ApiCallback;
import com.pip.phonexiaapi.ISpeechApi;
import com.pip.phonexiaapi.RealTimeCallback;
import com.pip.phonexiaapi.RecorderCallback;
import com.pip.phonexiaapi.SpeechApi;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.Segment;
import com.pip.phonexiaapi.data.Speaker;
import com.pip.phonexiaapi.data.SpeakersResult;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.unitskoda.recording.Recorder;

import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by filipsollar on 7.4.18.
 */

public class MeetingPresenter extends BasePresenter<MeetingContract.Screen> implements MeetingContract.Presenter {

    private static final String TAG = MeetingPresenter.class.getSimpleName();

    private ISpeechApi mApi;

    @Inject
    public MeetingPresenter(ISpeechApi api, MeetingContract.Screen screen) {
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
    public void createAndPrepareGroup(List<String> userModels, String groupName) {
        mApi.createAndPrepareGroup(userModels, groupName, new ApiCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result.booleanValue()) {
                    getScreen().speakerRecognitionPrepared();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void startListening() {

        final RecorderCallback callback = mApi.getCallback();

        mApi.realTimeProcessing(Recorder.RECORDER_SAMPLERATE, Language.CS_CZ, new RealTimeCallback<SpeechRecognitionResult>() {
            @Override
            public void onStarted() {
                Recorder.INSTANCE.start(new Function1<byte[], Unit>() {
                    @Override
                    public Unit invoke(byte[] bytes) {
                        callback.onRecording(bytes);
                        return null;
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onSpeakerResult(SpeakersResult result) {
                getScreen().showSpeaker(result.getResults().get(0).getSpeakerModel());
            }


            @Override
            public void onResult(SpeechRecognitionResult result) {
                StringBuilder sb = new StringBuilder();
                for (Segment segment: result.getRecognitionResult().getSegments()) {
                    sb.append(segment.getWord());
                    sb.append(", ");
                }

                getScreen().showText(sb.toString());
            }

            @Override
            public void finished() {

            }
        });


    }
}
