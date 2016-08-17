package com.mysticwind.android.bignerdranch.training.mycrimeintent.manager;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.CrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.List;
import java.util.UUID;

// TODO add update crime record API and not rely on model changes
public class CrimeLab {

    private final CrimeRecordDao crimeRecordDao;

    public CrimeLab(CrimeRecordDao crimeRecordDao) {
        this.crimeRecordDao = crimeRecordDao;
    }

    public List<CrimeRecord> getCrimeRecordList() {
        return crimeRecordDao.getCrimeRecords();
    }

    public CrimeRecord getCrimeRecord(UUID id) {
        return crimeRecordDao.getCrimeRecord(id);
    }

    public void addCrimeRecord(CrimeRecord crimeRecord) {
        crimeRecordDao.addCrimeRecord(crimeRecord);
    }

    public void deleteCrimeRecord(UUID id) {
        crimeRecordDao.deleteCrimeRecord(id);
    }
}
