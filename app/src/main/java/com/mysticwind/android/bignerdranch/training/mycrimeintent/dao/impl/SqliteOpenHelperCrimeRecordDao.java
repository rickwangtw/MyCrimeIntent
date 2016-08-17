package com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.CrimeRecordDao;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SqliteOpenHelperCrimeRecordDao extends SQLiteOpenHelper implements CrimeRecordDao{

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    private static class CrimeDbSchema {

        public static final class CrimeTable {
            public static final String NAME = "crimes";

            public static final class Column {
                public static final String UUID = "uuid";
                public static final String TITLE = "title";
                public static final String DATE = "date";
                public static final String SOLVED = "solved";
            }
        }
    }

    public class CrimeCursorWrapper extends CursorWrapper {
        public CrimeCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        public CrimeRecord getCrimeRecord() {
            String uuidString = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Column.UUID));
            String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Column.TITLE));
            long timestamp = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Column.DATE));
            int isSolved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Column.SOLVED));
            return new CrimeRecord(UUID.fromString(uuidString), title, new Date(timestamp), isSolved != 0);
        }
    }

    public SqliteOpenHelperCrimeRecordDao(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CrimeDbSchema.CrimeTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CrimeDbSchema.CrimeTable.Column.UUID + ", " +
                CrimeDbSchema.CrimeTable.Column.TITLE + ", " +
                CrimeDbSchema.CrimeTable.Column.DATE + ", " +
                CrimeDbSchema.CrimeTable.Column.SOLVED +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public List<CrimeRecord> getCrimeRecords() {
        List<CrimeRecord> crimeRecords = new ArrayList<>();
        CrimeCursorWrapper cursor = queryCrimeRecords(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimeRecords.add(cursor.getCrimeRecord());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimeRecords;
    }

    private CrimeCursorWrapper queryCrimeRecords(String whereClause, String[] whereArgs) {
        Cursor cursor = getReadableDatabase().query(
                CrimeDbSchema.CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

    @Override
    public CrimeRecord getCrimeRecord(UUID crimeId) {
        CrimeCursorWrapper cursor = queryCrimeRecords(
                CrimeDbSchema.CrimeTable.Column.UUID + " = ?",
                new String[] { crimeId.toString() }
        );
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrimeRecord();
        } finally {
            cursor.close();
        }
    }

    @Override
    public void addCrimeRecord(CrimeRecord crimeRecord) {
        ContentValues values = getContentValues(crimeRecord);
        getWritableDatabase().insert(CrimeDbSchema.CrimeTable.NAME, null, values);
    }

    @Override
    public void updateCrimeRecord(CrimeRecord crimeRecord) {
        String uuidString = crimeRecord.getId().toString();
        ContentValues values = getContentValues(crimeRecord);
        getWritableDatabase().update(
                CrimeDbSchema.CrimeTable.NAME,
                values,
                CrimeDbSchema.CrimeTable.Column.UUID + " = ?",
                new String[] { uuidString }
        );
    }

    @Override
    public void deleteCrimeRecord(UUID crimeId) {
        getWritableDatabase().delete(
                CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Column.UUID + " = ?",
                new String[] { crimeId.toString() }
        );
    }

    private static ContentValues getContentValues(CrimeRecord crimeRecord) {
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Column.UUID, crimeRecord.getId().toString());
        values.put(CrimeDbSchema.CrimeTable.Column.TITLE, crimeRecord.getTitle());
        values.put(CrimeDbSchema.CrimeTable.Column.DATE, crimeRecord.getDateTime().getTime());
        values.put(CrimeDbSchema.CrimeTable.Column.SOLVED, crimeRecord.isSolved() ? 1 : 0);
        return values;
    }
}
