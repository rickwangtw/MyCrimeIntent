package com.mysticwind.android.bignerdranch.training.mycrimeintent.application;

import android.app.Application;
import android.content.Context;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger.ApplicationComponent;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger.DaggerInitializer;

public class CrimeIntentApplication extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDagger();
    }

    private void initializeDagger() {
        component = DaggerInitializer.init(this);
    }

    public static ApplicationComponent component(Context context) {
        CrimeIntentApplication application = (CrimeIntentApplication) context.getApplicationContext();
        return application.component;
    }
}
