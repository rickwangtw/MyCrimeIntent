package com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.CrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl.DummyCrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    @Provides
    @Singleton
    public CrimeRecordDao provideCrimeRecordDao() {
        return new DummyCrimeRecordDao();
    }

    @Provides
    @Singleton
    public CrimeLab provideCrimeLab(CrimeRecordDao crimeRecordDao) {
        return new CrimeLab(crimeRecordDao);
    }

}
