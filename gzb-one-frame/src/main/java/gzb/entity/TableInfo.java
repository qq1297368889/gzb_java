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

import gzb.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {

    public String dbName;
    public String dbDesc;
    public String dbNameLowerCase;
    public String dbNameUpperCase;
    public String dbNameHumpLowerCase;
    public String dbNameHumpUpperCase;

    public String name;
    public String nameLowerCase;
    public String nameUpperCase;
    public String nameHumpLowerCase;
    public String nameHumpUpperCase;
    public String id;
    public String idLowerCase;
    public String idUpperCase;
    public String idHumpLowerCase;
    public String idHumpUpperCase;
    public String idType;
    public List<String> columnNames;
    public List<String> columnDesc;
    public List<String> columnNamesLowerCase;
    public List<String> columnNamesUpperCase;
    public List<String> columnNamesHumpLowerCase;
    public List<String> columnNamesHumpUpperCase;
    public List<String> columnTypes;
    public List<String> columnTypesDb;
    public List<Integer> columnSize;

    public void setColumnNames(List<String> list,String dbName) {
        this.dbName=dbName;
        dbNameLowerCase= Tools.lowStr_x(Tools.lowStr_hump(dbName));
        dbNameUpperCase=Tools.lowStr_d(Tools.lowStr_hump(dbName));
        dbNameHumpLowerCase=Tools.lowStr_x(Tools.lowStr_hump(dbName));
        dbNameHumpUpperCase=Tools.lowStr_d(Tools.lowStr_hump(dbName));

        nameLowerCase=Tools.lowStr_x(Tools.lowStr_hump(name));
        nameUpperCase=Tools.lowStr_d(Tools.lowStr_hump(name));
        nameHumpLowerCase=Tools.lowStr_x(Tools.lowStr_hump(name));
        nameHumpUpperCase=Tools.lowStr_d(Tools.lowStr_hump(name));

        idLowerCase=Tools.lowStr_x(Tools.lowStr_hump(id));
        idUpperCase=Tools.lowStr_d(Tools.lowStr_hump(id));
        idHumpLowerCase=Tools.lowStr_x(Tools.lowStr_hump(id));
        idHumpUpperCase=Tools.lowStr_d(Tools.lowStr_hump(id));

        columnNames = new ArrayList<>();
        columnNamesLowerCase = new ArrayList<>();
        columnNamesUpperCase = new ArrayList<>();
        columnNamesHumpLowerCase = new ArrayList<>();
        columnNamesHumpUpperCase = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            columnNames.add(list.get(i));
            columnNamesLowerCase.add(Tools.lowStr_x(Tools.lowStr_hump(list.get(i))));
            columnNamesUpperCase.add(Tools.lowStr_d(Tools.lowStr_hump(list.get(i))));
            columnNamesHumpLowerCase.add(Tools.lowStr_x(Tools.lowStr_hump(list.get(i))));
            columnNamesHumpUpperCase.add(Tools.lowStr_d(Tools.lowStr_hump(list.get(i))));
        }
    }


    public String getDbDesc() {
        return dbDesc;
    }

    public void setDbDesc(String dbDesc) {
        this.dbDesc = dbDesc;
    }

    public List<String> getColumnDesc() {
        return columnDesc;
    }

    public void setColumnDesc(List<String> columnDesc) {
        this.columnDesc = columnDesc;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbNameLowerCase() {
        return dbNameLowerCase;
    }

    public void setDbNameLowerCase(String dbNameLowerCase) {
        this.dbNameLowerCase = dbNameLowerCase;
    }

    public String getDbNameUpperCase() {
        return dbNameUpperCase;
    }

    public void setDbNameUpperCase(String dbNameUpperCase) {
        this.dbNameUpperCase = dbNameUpperCase;
    }

    public String getDbNameHumpLowerCase() {
        return dbNameHumpLowerCase;
    }

    public void setDbNameHumpLowerCase(String dbNameHumpLowerCase) {
        this.dbNameHumpLowerCase = dbNameHumpLowerCase;
    }

    public String getDbNameHumpUpperCase() {
        return dbNameHumpUpperCase;
    }

    public void setDbNameHumpUpperCase(String dbNameHumpUpperCase) {
        this.dbNameHumpUpperCase = dbNameHumpUpperCase;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameLowerCase() {
        return nameLowerCase;
    }

    public void setNameLowerCase(String nameLowerCase) {
        this.nameLowerCase = nameLowerCase;
    }

    public String getNameUpperCase() {
        return nameUpperCase;
    }

    public void setNameUpperCase(String nameUpperCase) {
        this.nameUpperCase = nameUpperCase;
    }

    public String getNameHumpLowerCase() {
        return nameHumpLowerCase;
    }

    public void setNameHumpLowerCase(String nameHumpLowerCase) {
        this.nameHumpLowerCase = nameHumpLowerCase;
    }

    public String getNameHumpUpperCase() {
        return nameHumpUpperCase;
    }

    public void setNameHumpUpperCase(String nameHumpUpperCase) {
        this.nameHumpUpperCase = nameHumpUpperCase;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLowerCase() {
        return idLowerCase;
    }

    public void setIdLowerCase(String idLowerCase) {
        this.idLowerCase = idLowerCase;
    }

    public String getIdUpperCase() {
        return idUpperCase;
    }

    public void setIdUpperCase(String idUpperCase) {
        this.idUpperCase = idUpperCase;
    }

    public String getIdHumpLowerCase() {
        return idHumpLowerCase;
    }

    public void setIdHumpLowerCase(String idHumpLowerCase) {
        this.idHumpLowerCase = idHumpLowerCase;
    }

    public String getIdHumpUpperCase() {
        return idHumpUpperCase;
    }

    public void setIdHumpUpperCase(String idHumpUpperCase) {
        this.idHumpUpperCase = idHumpUpperCase;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getColumnNamesLowerCase() {
        return columnNamesLowerCase;
    }

    public void setColumnNamesLowerCase(List<String> columnNamesLowerCase) {
        this.columnNamesLowerCase = columnNamesLowerCase;
    }

    public List<String> getColumnNamesUpperCase() {
        return columnNamesUpperCase;
    }

    public void setColumnNamesUpperCase(List<String> columnNamesUpperCase) {
        this.columnNamesUpperCase = columnNamesUpperCase;
    }

    public List<String> getColumnNamesHumpLowerCase() {
        return columnNamesHumpLowerCase;
    }

    public void setColumnNamesHumpLowerCase(List<String> columnNamesHumpLowerCase) {
        this.columnNamesHumpLowerCase = columnNamesHumpLowerCase;
    }

    public List<String> getColumnNamesHumpUpperCase() {
        return columnNamesHumpUpperCase;
    }

    public void setColumnNamesHumpUpperCase(List<String> columnNamesHumpUpperCase) {
        this.columnNamesHumpUpperCase = columnNamesHumpUpperCase;
    }

    public List<String> getColumnTypes() {
        return columnTypes;
    }

    public void setColumnTypes(List<String> columnTypes) {
        this.columnTypes = columnTypes;
    }

    public List<Integer> getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(List<Integer> columnSize) {
        this.columnSize = columnSize;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "dbName='" + dbName + '\'' +
                ", dbNameLowerCase='" + dbNameLowerCase + '\'' +
                ", dbNameUpperCase='" + dbNameUpperCase + '\'' +
                ", dbNameHumpLowerCase='" + dbNameHumpLowerCase + '\'' +
                ", dbNameHumpUpperCase='" + dbNameHumpUpperCase + '\'' +
                ", name='" + name + '\'' +
                ", nameLowerCase='" + nameLowerCase + '\'' +
                ", nameUpperCase='" + nameUpperCase + '\'' +
                ", nameHumpLowerCase='" + nameHumpLowerCase + '\'' +
                ", nameHumpUpperCase='" + nameHumpUpperCase + '\'' +
                ", id='" + id + '\'' +
                ", idLowerCase='" + idLowerCase + '\'' +
                ", idUpperCase='" + idUpperCase + '\'' +
                ", idHumpLowerCase='" + idHumpLowerCase + '\'' +
                ", idHumpUpperCase='" + idHumpUpperCase + '\'' +
                ", idType='" + idType + '\'' +
                ", columnNames=" + columnNames +
                ", columnNamesLowerCase=" + columnNamesLowerCase +
                ", columnNamesUpperCase=" + columnNamesUpperCase +
                ", columnNamesHumpLowerCase=" + columnNamesHumpLowerCase +
                ", columnNamesHumpUpperCase=" + columnNamesHumpUpperCase +
                ", columnTypes=" + columnTypes +
                ", columnSize=" + columnSize +
                '}';
    }
}
