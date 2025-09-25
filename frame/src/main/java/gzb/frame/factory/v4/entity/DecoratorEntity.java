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

package gzb.frame.factory.v4.entity;

import gzb.frame.annotation.DecoratorEnd;
import gzb.frame.annotation.DecoratorStart;
import gzb.frame.factory.GzbOneInterface;

import java.lang.reflect.Field;

public class DecoratorEntity {
    public GzbOneInterface call;
    public ClassEntity classEntity;
    public String name;
    public Field[]fields = null;
    public Class<?>[]fieldTypes = null;
    public String[]fieldNames;
    public DecoratorStart decoratorStart;
    public DecoratorEnd decoratorEnd;

}