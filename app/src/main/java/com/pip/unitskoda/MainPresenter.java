package com.pip.unitskoda;

import android.util.Log;

import com.kserno.baseclasses.BasePresenter;
import com.pip.phonexiaapi.ApiCallback;
import com.pip.phonexiaapi.ISpeechApi;


import java.util.List;

import javax.inject.Inject;


/**
 * Created by filipsollar on 6.4.18.
 */

public class MainPresenter extends BasePresenter<MainContract.Screen> implements MainContract.Presenter{

    private ISpeechApi mApi;

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
}
