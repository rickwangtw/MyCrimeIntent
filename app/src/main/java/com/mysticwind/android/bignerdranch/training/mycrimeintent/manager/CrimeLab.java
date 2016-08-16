package com.mysticwind.android.bignerdranch.training.mycrimeintent.manager;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private final List<CrimeRecord> crimeRecordList;

    public CrimeLab(List<CrimeRecord> crimeRecords) {
        this.crimeRecordList = crimeRecords;
    }

    public List<CrimeRecord> getCrimeRecordList() {
        return crimeRecordList;
    }

    public CrimeRecord getCrimeRecord(UUID id) {
        for (CrimeRecord crimeRecord : crimeRecordList) {
            if (crimeRecord.getId().equals(id)) {
                return crimeRecord;
            }
        }
        return null;
    }
}
