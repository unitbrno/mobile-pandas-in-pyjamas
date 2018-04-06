package com.pip.unitskoda;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kserno.baseclasses.BaseActivity;
import com.kserno.baseclasses.BasePresenter;
import com.pip.unitskoda.di.main.DaggerMainComponent;
import com.pip.unitskoda.di.main.MainComponent;
import com.pip.unitskoda.di.main.MainModule;
import com.pip.unitskoda.recording.Recorder;

import javax.inject.Inject;

/**
 * Created by filipsollar on 6.4.18.
 */

public class MainActivity extends BaseActivity implements MainContract.Screen{

    private MainComponent mComponent;

    @Inject MainPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mPresenter.startListening();
    }

    @Override
    protected void createPresenter() {
        getComponent().inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private MainComponent getComponent() {
        if (mComponent == null) {

            mComponent = DaggerMainComponent.builder()
                    .baseComponent(((BaseApplication) getApplication()).getBaseComponent())
                    .mainModule(new MainModule(this, Recorder.INSTANCE))
                    .build();
        }


        return mComponent;
    }

}
