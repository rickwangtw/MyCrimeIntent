package com.mysticwind.android.bignerdranch.training.mycrimeintent.config.dagger;

import android.content.Context;
import android.os.Environment;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.CrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl.DummyCrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl.GreenDaoCrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl.SqliteOpenHelperCrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private static final String DUMMY_CRIME_RECORD_DAO_NAME = "dummyCrimeRecordDao";
    private static final String SQLITE_OPEN_HELPER_CRIME_RECORD_DAO_NAME = "SqliteOpenHelperCrimeRecordDao";
    private static final String GREENDAO_CRIME_RECORD_DAO_NAME = "GreenDaoCrimeRecordDao";

    private final Context context;

    public ApplicationModule(Context context) {
        this.context = context;
    }

    @Provides
    @Named(DUMMY_CRIME_RECORD_DAO_NAME)
    @Singleton
    public CrimeRecordDao provideCrimeRecordDao() {
        return new DummyCrimeRecordDao();
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return context;
    }

    @Provides
    @Named(SQLITE_OPEN_HELPER_CRIME_RECORD_DAO_NAME)
    @Singleton
    public CrimeRecordDao provideSqliteOpenHelperCrimeRecordDao(Context context) {
        return new SqliteOpenHelperCrimeRecordDao(context);
    }

    @Provides
    @Named(GREENDAO_CRIME_RECORD_DAO_NAME)
    @Singleton
    public CrimeRecordDao provideGreenDaoCrimeRecordDao(Context context) {
        return new GreenDaoCrimeRecordDao(context);
    }

    @Provides
    @Singleton
    public CrimeLab provideCrimeLab(@Named(GREENDAO_CRIME_RECORD_DAO_NAME) CrimeRecordDao crimeRecordDao) {
        return new CrimeLab(crimeRecordDao);
    }

}
