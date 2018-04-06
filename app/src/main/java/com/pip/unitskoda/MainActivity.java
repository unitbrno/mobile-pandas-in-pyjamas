package com.pip.unitskoda;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kserno.baseclasses.BaseActivity;
import com.kserno.baseclasses.BasePresenter;
import com.pip.unitskoda.calendar.Attendee;
import com.pip.unitskoda.calendar.Calendar;
import com.pip.unitskoda.di.main.DaggerMainComponent;
import com.pip.unitskoda.di.main.MainComponent;
import com.pip.unitskoda.di.main.MainModule;
import com.pip.unitskoda.recording.Recorder;

import java.util.List;

import javax.inject.Inject;

import it.macisamuele.calendarprovider.CalendarInfo;
import it.macisamuele.calendarprovider.EventInfo;

/**
 * Created by filipsollar on 6.4.18.
 */

public class MainActivity extends BaseActivity implements MainContract.Screen {

    private MainComponent mComponent;

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Microphone permissions
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALENDAR)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        mPresenter.startListening();

                        CalendarInfo calendar = Calendar.getCalendars(MainActivity.this).get(0);
                        EventInfo event = Calendar.getCurrentEventsOfCalendar(MainActivity.this, calendar).get(0);
                        List<Attendee> attendees = Calendar.getAttendeesOfEvent(MainActivity.this, event);

                        Log.d("TAG", attendees.toString());
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                })
                .check();

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
