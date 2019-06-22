package com.chorestory.module;

import javax.inject.Singleton;

import dagger.Component;

import com.chorestory.app.MainActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
