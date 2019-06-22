package com.chorestory.module;

import javax.inject.Singleton;

import dagger.Component;

import com.chorestory.app.ChildLoginSignUpActivity;
import com.chorestory.app.MainActivity;
import com.chorestory.app.ParentLoginActivity;
import com.chorestory.app.ParentLoginSignUpActivity;
import com.chorestory.app.ParentSignUpActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(ParentLoginSignUpActivity activity);

    void inject(ParentLoginActivity activity);

    void inject(ParentSignUpActivity activity);

    void inject(ChildLoginSignUpActivity activity);
}
