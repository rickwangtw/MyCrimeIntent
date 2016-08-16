package com.mysticwind.android.bignerdranch.training.mycrimeintent.model;

import java.io.Serializable;

public class Time implements Serializable {

    private final int hour;
    private final int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
