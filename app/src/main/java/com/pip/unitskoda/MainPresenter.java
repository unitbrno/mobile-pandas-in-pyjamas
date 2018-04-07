package com.pip.unitskoda;

import android.util.Log;

import com.kserno.baseclasses.BasePresenter;
import com.pip.phonexiaapi.ApiCallback;
import com.pip.phonexiaapi.ISpeechApi;
import com.pip.phonexiaapi.RealTimeCallback;
import com.pip.phonexiaapi.RecorderCallback;
import com.pip.phonexiaapi.data.Language;
import com.pip.phonexiaapi.data.Segment;
import com.pip.phonexiaapi.data.Speaker;
import com.pip.phonexiaapi.data.SpeakersResult;
import com.pip.phonexiaapi.data.SpeechRecognitionResult;
import com.pip.unitskoda.calendar.Attendee;
import com.pip.unitskoda.recording.Recorder;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


/**
 * Created by filipsollar on 6.4.18.
 */

public class MainPresenter extends BasePresenter<MainContract.Screen> implements MainContract.Presenter{

    private ISpeechApi mApi;

    private List<Attendee> mAttendees;

    private static final String TAG = MainPresenter.class.getSimpleName();

    @Inject
    public MainPresenter(ISpeechApi api,MainContract.Screen screen) {
        super(screen);
        mApi = api;

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        stopStream();
    }

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

    public void startListening(List<Attendee> attendees) {

        mAttendees = attendees;

        final RecorderCallback callback = mApi.getCallback();

        mApi.realTimeProcessing(Recorder.RECORDER_SAMPLERATE, Language.ENGLISH, new RealTimeCallback<SpeechRecognitionResult>() {
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

                Speaker max = result.getResults().get(0);
                for (int i = 1; i < result.getResults().size(); i++) {
                    double speakerScore = result.getResults().get(i).getChannelScores().get(0).getScores().get(0).getScore();
                    if (max.getChannelScores().get(0).getScores().get(0).getScore() < speakerScore) {
                        max = result.getResults().get(i);
                    }
                }

                String name = "";
                for (Attendee attendee: mAttendees) {
                    if (max.getSpeakerModel().equals(attendee.getEmail())) {
                        name = attendee.getName();
                    }
                }

                getScreen().showSpeaker(name);
            }


            @Override
            public void onResult(SpeechRecognitionResult result) {
                List<String> text = new ArrayList<>();
                for (Segment segment: result.getRecognitionResult().getSegments()) {
                    text.add(segment.getWord());
                }

                getScreen().showText(text);
            }

            @Override
            public void finished() {

            }
        });


    }

    @Override
    public void loadModels() {
        mApi.getUserModels(new ApiCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                getScreen().showUserModels(result);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void stopStream() {
        Recorder.INSTANCE.stop();
        mApi.stopProcessing();
    }
}
