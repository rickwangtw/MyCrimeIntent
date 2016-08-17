package com.mysticwind.android.bignerdranch.training.mycrimeintent.dao.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CrimeRecordDto {
    @Id
    private String id;
    private String title;
    @NotNull
    private long timestamp;
    @NotNull
    private boolean solved;
    private String suspect;

    @Generated(hash = 1952728284)
    public CrimeRecordDto(String id, String title, long timestamp, boolean solved,
            String suspect) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.solved = solved;
        this.suspect = suspect;
    }

    @Generated(hash = 163280153)
    public CrimeRecordDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getSolved() {
        return this.solved;
    }

    public String getSuspect() {
        return suspect;
    }

    public void setSuspect(String suspect) {
        this.suspect = suspect;
    }
}
