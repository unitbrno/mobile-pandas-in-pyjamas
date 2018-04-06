package com.pip.unitskoda;

import android.app.Application;

import com.pip.unitskoda.di.BaseComponent;
import com.pip.unitskoda.di.BaseModule;

/**
 * Created by filipsollar on 6.4.18.
 */

public class BaseApplication extends Application {

    private BaseComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();


    }
}
