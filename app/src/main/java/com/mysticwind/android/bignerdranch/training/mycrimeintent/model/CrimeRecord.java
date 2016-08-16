package com.mysticwind.android.bignerdranch.training.mycrimeintent.model;

import java.util.Date;
import java.util.UUID;

public class CrimeRecord {

    private final UUID id;
    private String title;
    // TODO use Joda or JSR-310
    private final Date dateTime;
    private boolean solved;

    public CrimeRecord() {
        this(null);
    }

    public CrimeRecord(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.dateTime = new Date();
        this.solved = false;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
