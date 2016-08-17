package com.mysticwind.android.bignerdranch.training.mycrimeintent.dao;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.List;
import java.util.UUID;

public interface CrimeRecordDao {

    List<CrimeRecord> getCrimeRecords();
    CrimeRecord getCrimeRecord(UUID crimeId);
    void addCrimeRecord(CrimeRecord crimeRecord);
    void updateCrimeRecord(CrimeRecord crimeRecord);
    void deleteCrimeRecord(UUID crimeId);
}
