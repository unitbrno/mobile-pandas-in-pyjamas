package com.pip.unitskoda.di.user;

import com.pip.unitskoda.user.UserContract;

import dagger.Module;
import dagger.Provides;

/**
 * Created by filipsollar on 6.4.18.
 */

@Module
public class UserModule {

    private UserContract.Screen mScreen;

    public UserModule(UserContract.Screen screen) {
        mScreen = screen;
    }

    @Provides
    public UserContract.Screen providesScreen() {
        return mScreen;
    }
}
