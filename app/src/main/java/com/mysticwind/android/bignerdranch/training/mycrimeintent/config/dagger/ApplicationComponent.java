package com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimeFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimeListFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimePagerActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(CrimeListFragment crimeListFragment);

    void inject(CrimeFragment crimeFragment);

    void inject(CrimePagerActivity crimePagerActivity);
}
