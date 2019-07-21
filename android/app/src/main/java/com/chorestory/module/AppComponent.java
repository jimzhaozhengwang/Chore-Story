package com.chorestory.module;

import com.chorestory.adapter.QuestRecyclerViewAdapter;
import com.chorestory.app.CameraActivity;
import com.chorestory.app.ChildHomeActivity;
import com.chorestory.app.ChildJoinClanActivity;
import com.chorestory.app.ChildQuestDetailsActivity;
import com.chorestory.app.ParentCreateClanActivity;
import com.chorestory.app.MainActivity;
import com.chorestory.app.ParentHomeActivity;
import com.chorestory.app.ParentJoinClanActivity;
import com.chorestory.app.ParentLoginActivity;
import com.chorestory.app.ParentLoginSignUpActivity;
import com.chorestory.app.ParentQuestDetailsActivity;
import com.chorestory.app.ParentSignUpActivity;
import com.chorestory.app.QrCodeActivity;
import com.chorestory.app.SplashActivity;
import com.chorestory.fragment.ChildClanFragment;
import com.chorestory.fragment.ChildProfileFragment;
import com.chorestory.fragment.ChildFriendsFragment;
import com.chorestory.fragment.ChildQuestsFragment;
import com.chorestory.fragment.ParentClanFragment;
import com.chorestory.fragment.ParentCreateFragment;
import com.chorestory.fragment.ParentProfileFragment;
import com.chorestory.fragment.ParentQuestsFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class, AuthModule.class})
public interface AppComponent {

    // General pages
    void inject(MainActivity activity);

    void inject(SplashActivity activity);

    void inject(QrCodeActivity activity);

    void inject(CameraActivity activity);

    void inject(ParentCreateClanActivity activity);

    void inject(QuestRecyclerViewAdapter adapter);

    // Parent pages
    void inject(ParentLoginSignUpActivity activity);

    void inject(ParentLoginActivity activity);

    void inject(ParentSignUpActivity activity);

    void inject(ParentHomeActivity activity);

    void inject(ParentQuestDetailsActivity activity);

    void inject(ParentClanFragment fragment);

    void inject(ParentProfileFragment fragment);

    void inject(ParentCreateFragment fragment);

    void inject(ParentQuestsFragment fragment);

    // Child pages
    void inject(ChildJoinClanActivity activity);

    void inject(ChildHomeActivity activity);
  
    void inject(ChildQuestsFragment fragment);

    void inject(ChildQuestDetailsActivity activity);

    void inject(ChildClanFragment fragment);

    void inject(ChildProfileFragment fragment);

    void inject(ChildFriendsFragment fragment);

    void inject(ParentJoinClanActivity activity);
}
