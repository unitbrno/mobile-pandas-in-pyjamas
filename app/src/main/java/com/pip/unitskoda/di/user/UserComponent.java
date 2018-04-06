package com.pip.unitskoda.di.user;

import com.pip.unitskoda.di.BaseComponent;
import com.pip.unitskoda.user.UserActivity;
import com.pip.unitskoda.user.UserContract;

import dagger.Component;

/**
 * Created by filipsollar on 6.4.18.
 */

@Component(dependencies = BaseComponent.class, modules = UserModule.class)
public interface UserComponent {

    void inject(UserActivity activity);

}
