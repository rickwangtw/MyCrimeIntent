package com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.CrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DummyCrimeRecordDao implements CrimeRecordDao {

    private final List<CrimeRecord> crimeRecordList = new ArrayList<>();

    @Override
    // maybe we should provide an immutable list of immutable records
    public List<CrimeRecord> getCrimeRecords() {
        return crimeRecordList;
    }

    @Override
    // maybe we should provide an immutable one
    public CrimeRecord getCrimeRecord(UUID crimeId) {
        for (CrimeRecord crimeRecord : crimeRecordList) {
            if (crimeRecord.getId().equals(crimeId)) {
                return crimeRecord;
            }
        }
        return null;
    }

    @Override
    public void addCrimeRecord(CrimeRecord crimeRecord) {
        crimeRecordList.add(crimeRecord);
    }

    @Override
    public void updateCrimeRecord(CrimeRecord crimeRecord) {
        CrimeRecord persistedCrimeRecord = getCrimeRecord(crimeRecord.getId());
        if (persistedCrimeRecord == null) {
            throw new IllegalStateException("No crime record found for crime ID: " + crimeRecord.getId());
        }
        // update the persisted record so that we don't have to move the list items
        persistedCrimeRecord.setTitle(crimeRecord.getTitle());
        persistedCrimeRecord.updateDateTime(crimeRecord.getDateTime());
        persistedCrimeRecord.setSolved(crimeRecord.isSolved());
    }

    @Override
    public void deleteCrimeRecord(UUID crimeId) {
        Iterator<CrimeRecord> crimeRecordIterator = crimeRecordList.iterator();

        while (crimeRecordIterator.hasNext()) {
            CrimeRecord crimeRecord = crimeRecordIterator.next();
            if (crimeRecord.getId().equals(crimeId)) {
                crimeRecordIterator.remove();
                return;
            }
        }
    }
}
