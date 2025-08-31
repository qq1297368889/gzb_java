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
