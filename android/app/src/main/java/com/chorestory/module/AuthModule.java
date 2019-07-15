package com.chorestory.module;

import com.chorestory.helpers.TokenHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

// TODO: This entire file needs to change.
// For demo purposes so we can actually make later requests
@Module
public class AuthModule {

    @Singleton
    @Provides
    TokenHandler provideTokenHandler() {
        return new TokenHandler();
    }

}
