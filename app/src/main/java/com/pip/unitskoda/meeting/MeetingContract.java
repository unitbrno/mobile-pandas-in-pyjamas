package com.pip.unitskoda.meeting;

import java.util.List;

/**
 * Created by filipsollar on 7.4.18.
 */

public interface MeetingContract {
    interface Screen{
        void speakerRecognitionPrepared();
        void showText(String text);
        void showSpeaker(String name);
    }
    interface Presenter {
        void createAndPrepareGroup(List<String> userModels, String groupName);
        void startListening();
    }
}
