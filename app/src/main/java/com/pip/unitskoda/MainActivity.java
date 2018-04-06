package com.pip.unitskoda;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kserno.baseclasses.BaseActivity;
import com.kserno.baseclasses.BasePresenter;
import com.pip.unitskoda.calendar.Attendee;
import com.pip.unitskoda.calendar.CalendarManager;
import com.pip.unitskoda.calendar.ParticipantAdapter;
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

    private Spinner spCalendar;
    private TextView tvEventName;
    private RecyclerView rvParticipants;

    @Inject
    MainPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spCalendar = findViewById(R.id.spCalendar);
        tvEventName = findViewById(R.id.tvEventName);
        rvParticipants = findViewById(R.id.rvParticipants);

        // Microphone permissions
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_CALENDAR)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        onPermissionsGranted();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                })
                .check();

        setupCalendarSelects();
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

    private void onPermissionsGranted() {
        mPresenter.startListening();

    }

    private void setupCalendarSelects() {
        // Load calendars
        final List<CalendarInfo> calendars = CalendarManager.getCalendars(MainActivity.this);

        ArrayAdapter<String> calendarArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        calendarArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calendarArrayAdapter.addAll(CalendarManager.getCalendarStrings(calendars));

        spCalendar.setAdapter(calendarArrayAdapter);

        // Set event info from first calendar in list
        // TODO change default select to preference
        spCalendar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                updateEvent(calendars.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rvParticipants.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateEvent(CalendarInfo currentCalendar) {
        List<EventInfo> events = CalendarManager.getCurrentEventsOfCalendar(MainActivity.this, currentCalendar);

        if (events.isEmpty()) {
            // TODO display no pending events
        } else {
            EventInfo event = events.get(0);
            List<Attendee> attendees = CalendarManager.getAttendeesOfEvent(MainActivity.this, event);

            // Event card
            tvEventName.setText(event.getTitle());

            // Participants card
            ParticipantAdapter participantAdapter = new ParticipantAdapter();
            participantAdapter.setData(attendees);
            rvParticipants.setAdapter(participantAdapter);
        }

    }

}
