package com.pip.unitskoda.user;

import com.kserno.baseclasses.BasePresenter;
import com.pip.phonexiaapi.ApiCallback;
import com.pip.phonexiaapi.ISpeechApi;
import com.pip.phonexiaapi.data.AudioFileInfoResult;
import com.pip.unitskoda.recording.WavRecorder;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by filipsollar on 6.4.18.
 */

public class UserPresenter extends BasePresenter<UserContract.Screen> implements UserContract.Presenter {

    private ISpeechApi mApi;
    private WavRecorder mWavRecorder;
    private String mUsername;

    @Inject
    public UserPresenter(UserContract.Screen screen, ISpeechApi api) {
        super(screen);
        mApi = api;
        mWavRecorder = new WavRecorder("record.wav");
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void startRecording(String username) {
        mUsername = username;
        mWavRecorder.start();

    }

    @Override
    public void stopRecording() {
        File wavFile = mWavRecorder.stop();
        mApi.createSpeakerModel(mUsername, wavFile, new ApiCallback<AudioFileInfoResult>() {
            @Override
            public void onSuccess(AudioFileInfoResult result) {
                getScreen().success();
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                getScreen().connectionError(t.getMessage());
            }
        });

    }
}
