package com.pip.unitskoda.user;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface UserContract {
    interface Screen {
        void connectionError(String message);
        void success();
    }
    interface Presenter {
        void startRecording(String username, String path);
        void stopRecording();
    }
}
