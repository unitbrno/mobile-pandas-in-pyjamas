package com.pip.unitskoda.di;

import android.content.Context;

import com.pip.phonexiaapi.ISpeechApi;

import dagger.Module;
import dagger.Provides;

/**
 * Created by filipsollar on 6.4.18.
 */
@Module
public class BaseModule {

    private ISpeechApi mSpeechApi;
    private Context appContext;

    public BaseModule(Context appContext, ISpeechApi speechApi) {
        mSpeechApi = speechApi;
        this.appContext = appContext;
    }

    @Provides
    public ISpeechApi providesSpeechApi() {
        return mSpeechApi;
    }

    @Provides
    public Context providesContext() {
        return appContext;
    }

}
