package com.mysticwind.android.bignerdranch.training.mycrimeintent.model;

import org.threeten.bp.LocalDateTime;

import java.util.UUID;

public class CrimeRecord {

    private final UUID id;
    private String title;
    private final LocalDateTime dateTime;
    private boolean solved;

    public CrimeRecord(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.dateTime = LocalDateTime.now();
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
