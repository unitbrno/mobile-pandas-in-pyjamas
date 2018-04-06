package com.pip.unitskoda.di.main;

import com.pip.unitskoda.MainActivity;
import com.pip.unitskoda.di.BaseComponent;

import dagger.Component;

/**
 * Created by filipsollar on 6.4.18.
 */
@Component(modules = MainModule.class, dependencies = BaseComponent.class)
public interface MainComponent {

    void inject(MainActivity activity);

}
