package com.pip.unitskoda.di.meeting;

import com.pip.unitskoda.meeting.MeetingContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by filipsollar on 7.4.18.
 */
@Module
public class MeetingModule {

    private MeetingContract.Screen mScreen;

    public MeetingModule(MeetingContract.Screen screen) {
        mScreen = screen;
    }

    @Provides
    public MeetingContract.Screen providesScreen() {
        return mScreen;
    }

}
