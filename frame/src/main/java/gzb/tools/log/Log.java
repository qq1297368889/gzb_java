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

package gzb.tools.log;

public interface Log {
    public static Log create(){
        return new LogImpl();
    }
    public static Log create(Class<?>clazz){
        return new LogImpl(clazz);
    }
    public static Log log = new LogImpl();

    public void print(int index, Object... log);

    public void d(Object... log);

    public void i(Object... log);

    public void w(Object... log);

    public void e(Object... log);

    public void t(Object... log);

    public void s(String sql, long start, long end);
}
