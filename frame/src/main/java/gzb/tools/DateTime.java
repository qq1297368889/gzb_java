/*
 *
 *  * Copyright [2025] [GZB ONE]
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package gzb.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateTime {
    long t;
    String[] SJ = Tools.toArrayString(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd-HH-mm-ss",
            "yyyy/MM/dd/HH/mm/ss",
            "yyyy年MM月dd号HH点mm分ss秒",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd",
            "yyyy-MM",
            "yyyy",
            "HH:mm:ss",
            "HH:mm",
            "HH"
    );
    public DateTime() {
        t = System.currentTimeMillis();
    }
    public DateTime(Date date) {
        t = date.getTime();
    }
    public DateTime(java.sql.Timestamp timestamp) {
        t = timestamp.getTime();
    }
    public DateTime(java.time.LocalDateTime localDateTime,String zone) {
        this(localDateTime,ZoneId.of(zone));
    }
    public DateTime(java.time.LocalDateTime localDateTime) {
        this(localDateTime,ZoneId.systemDefault());
    }
    public DateTime(java.time.LocalDateTime localDateTime,ZoneId zoneId) {
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Instant instant = zdt.toInstant();
        t = instant.toEpochMilli(); // 毫秒级时间戳（13位）
        //long epochNano = instant.getNano();       // 纳秒数（0-999,999,999）
    }
    public DateTime(long t) {
        this.t = t;
    }

    public DateTime(int t) {
        this.t = t * 1000L;
    }

    public DateTime(String t, String format) throws ParseException {
        this.t = new SimpleDateFormat(format).parse(t).getTime();
    }

    public DateTime(String t, int i) throws ParseException {
        this.t = new SimpleDateFormat(SJ[i]).parse(t).getTime();
    }

    public DateTime(String t) throws ParseException {
        this.t = new SimpleDateFormat(SJ[0]).parse(t).getTime();
    }

    public int toStampInt() {
        return Integer.parseInt(String.valueOf(t /10));
    }

    public java.sql.Timestamp toTimestamp() {
        return new java.sql.Timestamp(t);
    }

    public long toStampLong() {
        return t;
    }


    public LocalDateTime toLocalDateTime(String zone) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(t),
                ZoneId.of(zone)
        );
    }
    public LocalDateTime toLocalDateTime(ZoneId zoneId) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(t),
                zoneId
        );
    }
    public LocalDateTime toLocalDateTime() {
        return toLocalDateTime(ZoneId.systemDefault());
    }

    public Date toDate() {
        return new Date(t);
    }

    @Override
    public String toString() {
        return formatDateTime();
    }

    public String formatYear() {
        return new SimpleDateFormat(SJ[7]).format(new Date(t));
    }

    public String formatMont() {
        return new SimpleDateFormat(SJ[6]).format(new Date(t));
    }

    public String formatDate() {
        return new SimpleDateFormat(SJ[4]).format(new Date(t));
    }

    public String formatTime() {
        return new SimpleDateFormat(SJ[5]).format(new Date(t));
    }

    public String formatDateTime() {
        return formatDateTime(SJ[0]);
    }

    public String formatDateTime(String format) {
        return new SimpleDateFormat(format).format(new Date(t));
    }

    /**
     * 时间运算 单位毫秒
     */
    public DateTime operation(long hm) {
        this.t += hm;
        return this;
    }

    /**
     * 时间运算 单位秒
     */
    public DateTime operation(int mm) {
        long m = mm;
        long j = m * 1000;
        this.t += j;
        return this;
    }
    public static DateTime valueOf(String t, String format) throws ParseException {
        return new DateTime(t, format);
    }
    public static DateTime valueOf(String t) throws ParseException {
        return new DateTime(t);
    }
    public static DateTime parseDateTime(String t, String format) throws ParseException {
        return new DateTime(t, format);
    }
    public static DateTime parseDateTime(String t) throws ParseException {
        return new DateTime(t);
    }
}
