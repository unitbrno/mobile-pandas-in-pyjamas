package com.pip.unitskoda.meeting;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.kserno.baseclasses.BaseActivity;
import com.kserno.baseclasses.BasePresenter;
import com.pip.unitskoda.BaseApplication;
import com.pip.unitskoda.R;
import com.pip.unitskoda.calendar.Attendee;
import com.pip.unitskoda.di.meeting.DaggerMeetingComponent;
import com.pip.unitskoda.di.meeting.MeetingComponent;
import com.pip.unitskoda.di.meeting.MeetingModule;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;

/**
 * Created by filipsollar on 7.4.18.
 */

public class MeetingActivity extends BaseActivity implements MeetingContract.Screen {

    // List of strings
    public static final String EXTRA_ATTENDEES = "EXTRA_ATTENDEES";
    public static final String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";

    @Inject
    MeetingPresenter mPresenter;

    private MeetingComponent mComponent;

    private TextView tvText;
    private TextView tvSpeaker;


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
        return R.layout.activity_meeting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tvSpeaker = findViewById(R.id.tvSpeakerName);
        tvText = findViewById(R.id.tvText);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();

        List<Attendee> attendees = (List<Attendee>) intent.getSerializableExtra(EXTRA_ATTENDEES);

        String groupName = intent.getStringExtra(EXTRA_EVENT_NAME);

        List<String> userModels = new ArrayList<>();


        for (Attendee attendee : attendees) {
//            if (attendee.getModels().contains(attendee.getEmail())) {
            userModels.add(attendee.getEmail());
//            }
        }
        groupName = groupName.replaceAll("\\s+", "");

        mPresenter.createAndPrepareGroup(userModels, groupName);

        // Microphone permissions
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_CALENDAR, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        mPresenter.startListening();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                })
                .check();

    }

    @Override
    public void speakerRecognitionPrepared() {
        Toasty.success(this, "SID connected").show();
    }

    @Override
    public void showText(String text) {
        tvText.setText(text);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void showSpeaker(String name) {
        tvSpeaker.setText(name);
    }

    private MeetingComponent getComponent() {
        if (mComponent == null) {
            mComponent = DaggerMeetingComponent.builder()
                    .baseComponent(((BaseApplication) getApplication()).getBaseComponent())
                    .meetingModule(new MeetingModule(this))
                    .build();
        }
        return mComponent;
    }
}
