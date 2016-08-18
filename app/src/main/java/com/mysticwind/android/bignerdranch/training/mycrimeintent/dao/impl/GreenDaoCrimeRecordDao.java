package com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.CrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.greendao.CrimeRecordDto;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.greendao.CrimeRecordDtoDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.greendao.DaoMaster;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.greendao.DaoSession;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class GreenDaoCrimeRecordDao implements CrimeRecordDao {

    private final DaoMaster.DevOpenHelper devOpenHelper;

    public GreenDaoCrimeRecordDao(Context context) {
        devOpenHelper = new DaoMaster.DevOpenHelper(context, "crime-records-db", null);
    }

    @Override
    public List<CrimeRecord> getCrimeRecords() {
        return queryAllCrimeRecords();
    }

    private List<CrimeRecord> queryAllCrimeRecords() {
        return queryCrimeRecords(null);
    }

    private List<CrimeRecord> queryCrimeRecords(UUID crimeId) {
        DaoMaster readableDaoMaster = getReadableDaoMaster();
        DaoSession session = readableDaoMaster.newSession();
        CrimeRecordDtoDao crimesDao = session.getCrimeRecordDtoDao();
        QueryBuilder<CrimeRecordDto> queryBuilder = crimesDao.queryBuilder();
        if (crimeId != null) {
            queryBuilder.where(CrimeRecordDtoDao.Properties.Id.eq(crimeId.toString()));
        }
        List<CrimeRecordDto> crimes = queryBuilder.list();
        session.clear();
        List<CrimeRecord> crimeRecords = new ArrayList<>(crimes.size());
        for (CrimeRecordDto crime : crimes) {
            crimeRecords.add(convert(crime));
        }
        return crimeRecords;
    }

    private CrimeRecord convert(CrimeRecordDto crime) {
        return new CrimeRecord(
                UUID.fromString(crime.getId()),
                crime.getTitle(),
                new Date(crime.getTimestamp()),
                crime.isSolved(),
                crime.getSuspect());
    }

    private CrimeRecordDto convert(CrimeRecord crimeRecord) {
        CrimeRecordDto crime = new CrimeRecordDto();
        crime.setId(crimeRecord.getId().toString());
        crime.setTitle(crimeRecord.getTitle());
        crime.setTimestamp(crimeRecord.getDateTime().getTime());
        crime.setSolved(crimeRecord.isSolved());
        crime.setSuspect(crimeRecord.getSuspect());
        return crime;
    }

    @Override
    public CrimeRecord getCrimeRecord(UUID crimeId) {
        List<CrimeRecord> crimeRecords = queryCrimeRecords(crimeId);
        if (crimeRecords.isEmpty()) {
            return null;
        } else if (crimeRecords.size() != 1) {
            throw new IllegalStateException("More than one crime records found for UUID: " + crimeId);
        } else {
            return crimeRecords.get(0);
        }
    }

    @Override
    public void addCrimeRecord(CrimeRecord crimeRecord) {
        DaoMaster daoMaster = getWritableDaoMaster();
        DaoSession session = daoMaster.newSession();
        CrimeRecordDtoDao crimesDao = session.getCrimeRecordDtoDao();
        crimesDao.insert(convert(crimeRecord));
        session.clear();
    }

    @Override
    public void updateCrimeRecord(CrimeRecord crimeRecord) {
        DaoMaster daoMaster = getWritableDaoMaster();
        DaoSession session = daoMaster.newSession();
        CrimeRecordDtoDao crimesDao = session.getCrimeRecordDtoDao();
        crimesDao.update(convert(crimeRecord));
        session.clear();
    }

    @Override
    public void deleteCrimeRecord(UUID crimeId) {
        DaoMaster daoMaster = getWritableDaoMaster();
        DaoSession session = daoMaster.newSession();
        CrimeRecordDtoDao crimesDao = session.getCrimeRecordDtoDao();
        CrimeRecordDto dto = new CrimeRecordDto();
        dto.setId(crimeId.toString());
        crimesDao.delete(dto);
        session.clear();
    }

    public DaoMaster getWritableDaoMaster() {
        return new DaoMaster(getWritableDatabase());
    }

    private SQLiteDatabase getWritableDatabase() {
        return devOpenHelper.getWritableDatabase();
    }

    public DaoMaster getReadableDaoMaster() {
        return new DaoMaster(getReadableDatabase());
    }

    private SQLiteDatabase getReadableDatabase() {
        return devOpenHelper.getReadableDatabase();
    }
}
