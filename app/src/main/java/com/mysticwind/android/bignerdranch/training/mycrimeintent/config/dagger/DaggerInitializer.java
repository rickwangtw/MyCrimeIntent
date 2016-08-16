package com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger;

import android.app.Application;

public class DaggerInitializer {

    public static ApplicationComponent init(Application application) {
        ApplicationComponent component = DaggerApplicationComponent.builder()
                .build();
        return component;
    }
}
