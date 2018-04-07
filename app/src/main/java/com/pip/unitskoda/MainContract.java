package com.pip.unitskoda;

import java.util.List;

/**
 * Created by filipsollar on 6.4.18.
 */

public interface MainContract {
    interface Screen {
        void showUserModels(List<String> userModels);
    }
    interface Presenter {
        void loadModels();
    }
}
