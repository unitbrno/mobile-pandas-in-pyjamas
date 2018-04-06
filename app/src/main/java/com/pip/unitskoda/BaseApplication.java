package com.pip.unitskoda;

import android.app.Application;

import com.pip.phonexiaapi.SpeechApi;
import com.pip.unitskoda.di.BaseComponent;
import com.pip.unitskoda.di.BaseModule;
import com.pip.unitskoda.di.DaggerBaseComponent;

import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;

/**
 * Created by filipsollar on 6.4.18.
 */

public class BaseApplication extends Application {

    private BaseComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        SpeechApi api = new SpeechApi();

        mComponent = DaggerBaseComponent.builder()
                .baseModule(new BaseModule(getApplicationContext(), api))
                .build();

        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                // Great!
            }
            @Override
            public void onFailure(Exception error) {
                // FFmpeg is not supported by device
            }
        });
    }



    public BaseComponent getBaseComponent() {
        return mComponent;
    }
}
