package com.pip.unitskoda.user;

import com.kserno.baseclasses.BasePresenter;
import com.pip.phonexiaapi.ApiCallback;
import com.pip.phonexiaapi.ISpeechApi;
import com.pip.phonexiaapi.data.AudioFileInfoResult;
import com.pip.unitskoda.recording.ExtAudioRecorder;
import com.pip.unitskoda.recording.WavRecorder;

import java.io.File;

import javax.inject.Inject;

/**
 * Created by filipsollar on 6.4.18.
 */

public class UserPresenter extends BasePresenter<UserContract.Screen> implements UserContract.Presenter {

    private ISpeechApi mApi;
    private ExtAudioRecorder mWavRecorder;
    private String mUsername;

    @Inject
    public UserPresenter(UserContract.Screen screen, ISpeechApi api, ExtAudioRecorder recorder) {
        super(screen);
        mApi = api;
        mWavRecorder = recorder;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {
        mWavRecorder.release();
    }

    @Override
    public void startRecording(String username, String fileName) {
        mUsername = username;
        mWavRecorder.startRecording(fileName);

    }

    @Override
    public void stopRecording() {
        mWavRecorder.stop(new ExtAudioRecorder.Callback() {
            @Override
            public void onSuccess(File file) {
                mApi.createSpeakerModel(mUsername, file, new ApiCallback<AudioFileInfoResult>() {
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

            @Override
            public void onFail(Throwable t) {
                t.printStackTrace();
            }
        });


    }
}
