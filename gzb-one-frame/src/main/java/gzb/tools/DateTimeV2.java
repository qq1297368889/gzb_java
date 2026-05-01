package gzb.tools;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeV2 {
    public final static DateTimeFormatter [] FORMATTER =new DateTimeFormatter []{
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm/ss"),
            DateTimeFormatter.ofPattern("yyyy年MM月dd号HH点mm分ss秒"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("yyyy-MM"),
            DateTimeFormatter.ofPattern("yyyy"),
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("HH"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    };
    private final long timeStamp;

    public DateTimeV2(long timeStamp) {
        this.timeStamp = timeStamp;

    }
    public DateTimeV2(Date date){
        this.timeStamp = date.getTime();
    }
    public DateTimeV2(Time time){
        this.timeStamp = time.getTime();
    }
    public DateTimeV2(java.util.Date date){
        this.timeStamp = date.getTime();
    }

    public long getTimeStamp() {
        return timeStamp;
    }

}
