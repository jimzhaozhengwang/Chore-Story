package com.chorestory.app;

import android.app.Application;

import com.chorestory.module.AppModule;
import com.chorestory.module.DaggerAppComponent;
import com.chorestory.module.AppComponent;
import com.chorestory.module.NetModule;


public class App extends Application {
    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule())
                .build();
    }
}