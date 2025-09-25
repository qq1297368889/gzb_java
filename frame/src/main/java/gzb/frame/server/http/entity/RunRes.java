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

package gzb.frame.server.http.entity;

public class RunRes {
    private int state = 0;
    private Object data;
    private Object msg;
    private long start;
    private long end;

    public Object getMsg() {
        return msg;
    }


    public int getState() {
        return state;
    }


    public Object getData() {
        return data;
    }


    public long getStart() {
        return start;
    }



    public long getEnd() {
        return end;
    }

    public RunRes setEnd(long end) {
        this.end = end;
        return this;
    }public RunRes setStart(long start) {
        this.start = start;
        return this;
    }
    public RunRes setData(Object data) {
        this.data = data;
        return this;
    }
    public RunRes setState(int state) {
        this.state = state;
        return this;
    }
    public RunRes setMsg(Object msg) {
        this.msg = msg;
        return this;
    }
    public RunRes success() {
        success(null);
        return this;
    }
    public RunRes success(Object data) {
        setState(200);
        setData(data);
        return this;
    }
    public RunRes fail() {
        setState(404);
        return this;
    }
    public RunRes err() {
        setState(500);
        return this;
    }

    @Override
    public String toString() {
        return "RunRes{" +
                "state=" + state +
                ", data=" + data +
                ", start=" + start +
                ", end=" + end +
                ", time=" + (end-start)+"ns" +
                ", msg=" + msg +
                '}';
    }
}
