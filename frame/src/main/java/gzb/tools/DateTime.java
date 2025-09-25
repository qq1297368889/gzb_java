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
import java.util.Date;

public class DateTime {
    long t;
    String[] SJ = Tools.toArrayString(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd-HH-mm-ss",
            "yyyy/MM/dd/HH/mm/ss",
            "yyyy年MM月dd号HH点mm分ss秒",
            "yyyy-MM-dd",
            "HH:mm:ss",
            "yyyy-MM",
            "yyyy"
    );
    public DateTime() {
        t = System.currentTimeMillis();
    }

    public DateTime(long t) {
        this.t = t;
    }

    public DateTime(int t) {
        this.t = t * 1000;
    }

    public DateTime(String t, String format) {
        try {
            this.t = new SimpleDateFormat(format).parse(t).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DateTime(String t, int i) {
        try {
            this.t = new SimpleDateFormat(SJ[i]).parse(t).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DateTime(String t) {
        try {
            this.t = new SimpleDateFormat(SJ[0]).parse(t).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int toStampInt() {
        return Integer.valueOf((t + "").substring(0, 10));
    }

    public long toStampLong() {
        return t;
    }

    public Date toDate() {
        return new Date(t);
    }

    @Override
    public String toString() {
        return formatDateTime();
    }

    public String formatYear() {
        return new SimpleDateFormat(SJ[7]).format(new Date(t)).toString();
    }

    public String formatMont() {
        return new SimpleDateFormat(SJ[6]).format(new Date(t)).toString();
    }

    public String formatDate() {
        return new SimpleDateFormat(SJ[4]).format(new Date(t)).toString();
    }

    public String formatTime() {
        return new SimpleDateFormat(SJ[5]).format(new Date(t)).toString();
    }

    public String formatDateTime() {
        return formatDateTime(SJ[0]);
    }

    public String formatDateTime(String format) {
        String time = new SimpleDateFormat(format).format(new Date(t)).toString();
        return time;
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
}
