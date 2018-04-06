package com.pip.unitskoda.di.main;

import com.pip.unitskoda.MainContract;
import com.pip.unitskoda.recording.Recorder;

import dagger.Module;
import dagger.Provides;

/**
 * Created by filipsollar on 6.4.18.
 */
@Module
public class MainModule {

    private MainContract.Screen mScreen;
    private Recorder mRecorder;

    public MainModule(MainContract.Screen screen, Recorder recorder) {
        mScreen = screen;
        mRecorder = recorder;
    }

    @Provides
    public MainContract.Screen getScreen() {
        return mScreen;
    }

    @Provides
    public Recorder getRecorder() {
        return mRecorder;
    }
}
