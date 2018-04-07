package com.pip.unitskoda.di;

import android.content.Context;

import com.pip.phonexiaapi.ISpeechApi;

import dagger.Component;

/**
 * Created by filipsollar on 6.4.18.
 */
@Component(modules = BaseModule.class)
public interface BaseComponent {

    Context getContext();
    ISpeechApi getSpeechApi();

}
