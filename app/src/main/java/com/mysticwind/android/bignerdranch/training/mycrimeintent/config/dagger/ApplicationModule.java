package com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.CrimeListActivity;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public List<CrimeRecord> provideCrimeRecords() {
        List<CrimeRecord> crimeRecordList = new ArrayList<>();
        // we are now capable of create crime records.
        return crimeRecordList;
    }

    @Provides
    @Singleton
    public CrimeLab provideCrimeLab(List<CrimeRecord> crimeRecords) {
        return new CrimeLab(crimeRecords);
    }

}
