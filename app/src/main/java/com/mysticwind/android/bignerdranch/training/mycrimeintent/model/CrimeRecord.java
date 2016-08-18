package com.mysticwind.android.bignerdranch.training.mycrimeintent.model;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CrimeRecord {

    private final UUID id;
    private String title;
    // TODO use Joda or JSR-310
    private Date dateTime;
    private boolean solved;
    private String suspect;

    public CrimeRecord() {
        this("");
    }

    public CrimeRecord(String title) {
        this(UUID.randomUUID(), title, new Date(), false);
    }

    public CrimeRecord(UUID id, String title, Date dateTime, boolean solved) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.solved = solved;
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

    public void updateDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void updateDate(Date date) {
        Time originalTime = getTime(this.dateTime);
        this.dateTime = date;
        updateTime(originalTime);
    }

    private Time getTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int originalHour = calendar.get(Calendar.HOUR_OF_DAY);
        int originalMinute = calendar.get(Calendar.MINUTE);
        return new Time(originalHour, originalMinute);
    }

    public void updateTime(Time time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.dateTime);
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.dateTime = calendar.getTime();
    }

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }
}
