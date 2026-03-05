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
    private Timestamp timestamp;
    private Date date;
    private DateTime dateTime;

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

}





































