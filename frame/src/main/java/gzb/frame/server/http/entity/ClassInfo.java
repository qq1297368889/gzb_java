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

import gzb.frame.annotation.Controller;
import gzb.frame.annotation.Decorator;
import gzb.frame.annotation.RequestMapping;
import gzb.frame.annotation.Service;

import java.util.List;
import java.util.Map;

public class ClassInfo {
    Class aClass;
    String objectKey;
    RequestMapping requestMapping;
    Controller controller;
    Service service;
    Decorator decorator;
    List<FieldInfo>listFieldInfo;
    Map<String, MethodInfo> mapMethodInfo;

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public List<FieldInfo> getListFieldInfo() {
        return listFieldInfo;
    }

    public void setListFieldInfo(List<FieldInfo> listFieldInfo) {
        this.listFieldInfo = listFieldInfo;
    }

    public Map<String, MethodInfo> getMapMethodInfo() {
        return mapMethodInfo;
    }

    public void setMapMethodInfo(Map<String, MethodInfo> mapMethodInfo) {
        this.mapMethodInfo = mapMethodInfo;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public void setDecorator(Decorator decorator) {
        this.decorator = decorator;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "aClass=" + aClass +
                ", objectKey='" + objectKey + '\'' +
                ", requestMapping=" + requestMapping +
                ", controller=" + controller +
                ", service=" + service +
                ", decorator=" + decorator +
                ", listFieldInfo=" + listFieldInfo +
                ", mapMethodInfo=" + mapMethodInfo +
                '}';
    }
}
