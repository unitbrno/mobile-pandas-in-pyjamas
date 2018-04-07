package com.pip.unitskoda.di.meeting;

import com.pip.unitskoda.di.BaseComponent;
import com.pip.unitskoda.meeting.MeetingActivity;

import dagger.Component;

/**
 * Created by filipsollar on 7.4.18.
 */
@Component(dependencies = BaseComponent.class, modules = MeetingModule.class)
public interface MeetingComponent {

    void inject(MeetingActivity activity);

}
