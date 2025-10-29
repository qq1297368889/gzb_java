package com.frame.api;

import gzb.tools.DateTime;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class TestEntity {
 /*
    public LocalDateTime localDateTime;
    public LocalDateTime[] localDateTimes;
    public Timestamp timestamp;
    public Timestamp[] timestamps;
    public Date date;
    public Date[] dates;
    public DateTime dateTime;
    public DateTime[] dateTimes;

*/

    private LocalDateTime localDateTime;
    private LocalDateTime[] localDateTimes;
    private Timestamp timestamp;
    private Timestamp[] timestamps;
    private Date date;
    private Date[] dates;
    private DateTime dateTime;
    private DateTime[] dateTimes;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public LocalDateTime[] getLocalDateTimes() {
        return localDateTimes;
    }

    public void setLocalDateTimes(LocalDateTime[] localDateTimes) {
        this.localDateTimes = localDateTimes;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp[] getTimestamps() {
        return timestamps;
    }

    public void setTimestamps(Timestamp[] timestamps) {
        this.timestamps = timestamps;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date[] getDates() {
        return dates;
    }

    public void setDates(Date[] dates) {
        this.dates = dates;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime[] getDateTimes() {
        return dateTimes;
    }

    public void setDateTimes(DateTime[] dateTimes) {
        this.dateTimes = dateTimes;
    }
}





































