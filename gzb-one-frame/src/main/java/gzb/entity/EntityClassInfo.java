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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassInfo {
    public Class aClass;
    public String name;
    public int keyIndex;
    public List<String> attributes = new ArrayList<>();
    public List<String> columnNames = new ArrayList<>();
    public List<String> notes = new ArrayList<>();


    public List<Field> fields = new ArrayList<>();
    public List<Object> values = new ArrayList<>();
    public List<Integer> sizes = new ArrayList<>();
    //public List<Class> types = new ArrayList<>();

    @Override
    public String toString() {
        return "EntityClassInfo{" +
                "aClass=" + aClass +
                ", name='" + name + '\'' +
                ", keyIndex=" + keyIndex +
                ", attributes=" + attributes +
                ", columnNames=" + columnNames +
                ", values=" + values +
                ", sizes=" + sizes +
                //", types=" + types +
                ", fields=" + fields +
                '}';
    }
}
