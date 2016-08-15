package com.mysticwind.android.bignerdranch.training.mycrimeintent.model;

import java.util.UUID;

public class CrimeRecord {

    private final UUID id;
    private final String title;

    public CrimeRecord(String title) {
        this.id = UUID.randomUUID();
        this.title = title;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
