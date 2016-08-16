package com.mysticwind.android.bignerdranch.training.mycrimeintent.manager;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum CrimeLab {
    INSTANCE;

    private final List<CrimeRecord> crimeRecordList;

    CrimeLab() {
        crimeRecordList = new ArrayList<>();
        for (int index = 0; index < 100; index++) {
            CrimeRecord crime = new CrimeRecord();
            crime.setTitle("Crime #" + index);
            crime.setSolved(index % 2 == 0);
            crimeRecordList.add(crime);
        }
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
