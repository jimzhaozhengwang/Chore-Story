package com.chorestory.module;

import javax.inject.Singleton;

import dagger.Component;

import com.chorestory.app.ChildHomeActivity;
import com.chorestory.app.ChildJoinClanActivity;
import com.chorestory.app.CreateClanActivity;
import com.chorestory.app.MainActivity;
import com.chorestory.app.ParentHomeActivity;
import com.chorestory.app.ParentLoginActivity;
import com.chorestory.app.ParentLoginSignUpActivity;
import com.chorestory.app.ParentQuestDetailsActivity;
import com.chorestory.app.ParentSignUpActivity;
import com.chorestory.app.SplashActivity;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, AuthModule.class})
public interface AppComponent {
    void inject(MainActivity activity);

    void inject(ParentLoginSignUpActivity activity);

    void inject(ParentLoginActivity activity);

    void inject(ParentSignUpActivity activity);

    void inject(CreateClanActivity activity);

    void inject(ParentHomeActivity activity);

    void inject(ChildJoinClanActivity activity);

    void inject(ChildHomeActivity activity);

    void inject(ParentQuestDetailsActivity activity);

    void inject(SplashActivity activity);
}
