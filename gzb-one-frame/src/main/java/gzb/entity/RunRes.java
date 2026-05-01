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

package gzb.entity;

public class RunRes {
    private int state = 0;
    private Object data;
    public RunRes(int state){
        this.state = state;
    }
    public RunRes(){
        this.state = 200;
    }
    public int getState() {
        return state;
    }


    public Object getData() {
        return data;
    }



    public RunRes setData(Object data) {
        this.data = data;
        return this;
    }
    public RunRes setState(int state) {
        this.state = state;
        return this;
    }
    public RunRes success() {
        return success(null);
    }
    public RunRes fail() {
       return fail(null);
    }
    public RunRes err() {
        return err(null);
    }
    public RunRes intercept() {
        return intercept(null);
    }
    public RunRes success(Object responseData) {
        setState(200);
        setData(responseData);
        return this;
    }
    public RunRes fail(Object responseData) {
        setState(404);
        setData(responseData);
        return this;
    }
    public RunRes err(Object responseData) {
        setState(500);
        setData(responseData);
        return this;
    }
    public RunRes intercept(Object responseData) {
        setState(400);
        setData(responseData);
        return this;
    }
    public RunRes release() {
        return release(null);
    }
    public RunRes release(Object responseData) {
        return success(responseData);
    }

    @Override
    public String toString() {
        return "RunRes{" +
                "state=" + state +
                ", data=" + data +
                '}';
    }
}
