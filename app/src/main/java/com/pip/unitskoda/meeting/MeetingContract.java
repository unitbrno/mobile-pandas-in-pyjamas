package com.pip.unitskoda.meeting;

import com.pip.unitskoda.calendar.Attendee;

import java.util.List;

/**
 * Created by filipsollar on 7.4.18.
 */

public interface MeetingContract {
    interface Screen{
        void speakerRecognitionPrepared();
        void showText(List<String> text);
        void showSpeaker(String name);
        void showText(String text);
    }
    interface Presenter {
        void createAndPrepareGroup(List<String> userModels, String groupName);
        void startListening(List<Attendee> attendees);
    }
}
