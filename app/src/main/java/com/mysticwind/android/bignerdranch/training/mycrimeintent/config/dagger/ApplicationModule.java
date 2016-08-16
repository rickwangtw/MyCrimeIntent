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
        for (int index = 0; index < 100; index++) {
            CrimeRecord crime = new CrimeRecord();
            crime.setTitle("Crime #" + index);
            crime.setSolved(index % 2 == 0);
            crimeRecordList.add(crime);
        }
        return crimeRecordList;
    }

    @Provides
    @Singleton
    public CrimeLab provideCrimeLab(List<CrimeRecord> crimeRecords) {
        return new CrimeLab(crimeRecords);
    }

}
