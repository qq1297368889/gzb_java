package com.frame;

import gzb.entity.SqlTemplate;
import com.frame.entity.*;
import gzb.tools.OnlyId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlTools {
    public static boolean appSql(String key, Object val, StringBuilder stringBuilder, List<Object> list, boolean where, boolean and) {
        if (val != null) {
            if (where) {
                stringBuilder.append(" where");
            }
            if (and){
                stringBuilder.append(" and");
            }
            stringBuilder.append(" ").append(key).append(" = ?");
            list.add(val);
            return true;
        }
        return false;
    }
    public static SqlTemplate toSelectSql(SysFile sysFile) {
        return toSelectSql(sysFile,"sys_file_id", "asc", 0, false);
    }
    /**
     * 实体类[SysFile]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysFile sysFile,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_file");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysFile,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysFile sysFile,java.lang.Long sysFileId) {
        if (sysFileId > 0L) {
            sysFile.setSysFileId(sysFileId);
        } else {
            sysFile.setSysFileId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_file(");
        List<Object> list = new ArrayList<>();
        if (sysFile.getSysFileId() != null && !sysFile.getSysFileId().toString().isEmpty()) {
            sb.append("sys_file_id,");
            list.add(sysFile.getSysFileId());
        }
        if (sysFile.getSysFilePath() != null && !sysFile.getSysFilePath().toString().isEmpty()) {
            sb.append("sys_file_path,");
            list.add(sysFile.getSysFilePath());
        }
        if (sysFile.getSysFileMd5() != null && !sysFile.getSysFileMd5().toString().isEmpty()) {
            sb.append("sys_file_md5,");
            list.add(sysFile.getSysFileMd5());
        }
        if (sysFile.getSysFileTime() != null && !sysFile.getSysFileTime().toString().isEmpty()) {
            sb.append("sys_file_time,");
            list.add(sysFile.getSysFileTime());
        }
        if (sysFile.getSysFileType() != null && !sysFile.getSysFileType().toString().isEmpty()) {
            sb.append("sys_file_type,");
            list.add(sysFile.getSysFileType());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysFile sysFile) {
        if (sysFile.getSysFileId() == null || sysFile.getSysFileId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_file set");
        List<Object> list = new ArrayList<>();
        if (sysFile.getSysFilePath() != null && !sysFile.getSysFilePath().toString().isEmpty()) {
            sb.append(" sys_file_path = ?,");
            list.add(sysFile.getSysFilePath());
        }
        if (sysFile.getSysFileMd5() != null && !sysFile.getSysFileMd5().toString().isEmpty()) {
            sb.append(" sys_file_md5 = ?,");
            list.add(sysFile.getSysFileMd5());
        }
        if (sysFile.getSysFileTime() != null && !sysFile.getSysFileTime().toString().isEmpty()) {
            sb.append(" sys_file_time = ?,");
            list.add(sysFile.getSysFileTime());
        }
        if (sysFile.getSysFileType() != null && !sysFile.getSysFileType().toString().isEmpty()) {
            sb.append(" sys_file_type = ?,");
            list.add(sysFile.getSysFileType());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_file_id = ?");
        list.add(sysFile.getSysFileId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysFile sysFile,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_file");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysFile,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysFile sysFile,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_file_id", sysFile.getSysFileId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_file_id", sysFile.getSysFileId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_file_path", sysFile.getSysFilePath(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_file_md5", sysFile.getSysFileMd5(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_file_time", sysFile.getSysFileTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_file_type", sysFile.getSysFileType(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysFile asSysFile(Map<String,String[]> requestData){
        List<SysFile> list=asListSysFile(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysFile> asListSysFile(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysFileIdArray = requestData.get("sysFileId");
        if (sysFileIdArray!=null && sysFileIdArray.length>0) {
            if (size<sysFileIdArray.length) {
                size=sysFileIdArray.length;
            }
        }
        String[] sysFilePathArray = requestData.get("sysFilePath");
        if (sysFilePathArray!=null && sysFilePathArray.length>0) {
            if (size<sysFilePathArray.length) {
                size=sysFilePathArray.length;
            }
        }
        String[] sysFileMd5Array = requestData.get("sysFileMd5");
        if (sysFileMd5Array!=null && sysFileMd5Array.length>0) {
            if (size<sysFileMd5Array.length) {
                size=sysFileMd5Array.length;
            }
        }
        String[] sysFileTimeArray = requestData.get("sysFileTime");
        if (sysFileTimeArray!=null && sysFileTimeArray.length>0) {
            if (size<sysFileTimeArray.length) {
                size=sysFileTimeArray.length;
            }
        }
        String[] sysFileTypeArray = requestData.get("sysFileType");
        if (sysFileTypeArray!=null && sysFileTypeArray.length>0) {
            if (size<sysFileTypeArray.length) {
                size=sysFileTypeArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysFile> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysFile sysFile=new SysFile();
            if (sysFileIdArray!=null && i <= sysFileIdArray.length-1) {
                sysFile.setSysFileId(java.lang.Long.valueOf(sysFileIdArray[i]));
            }
            if (sysFilePathArray!=null && i <= sysFilePathArray.length-1) {
                sysFile.setSysFilePath(java.lang.String.valueOf(sysFilePathArray[i]));
            }
            if (sysFileMd5Array!=null && i <= sysFileMd5Array.length-1) {
                sysFile.setSysFileMd5(java.lang.String.valueOf(sysFileMd5Array[i]));
            }
            if (sysFileTimeArray!=null && i <= sysFileTimeArray.length-1) {
                sysFile.setSysFileTime(java.lang.String.valueOf(sysFileTimeArray[i]));
            }
            if (sysFileTypeArray!=null && i <= sysFileTypeArray.length-1) {
                sysFile.setSysFileType(java.lang.String.valueOf(sysFileTypeArray[i]));
            }
            list.add(sysFile);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysGroup sysGroup) {
        return toSelectSql(sysGroup,"sys_group_id", "asc", 0, false);
    }
    /**
     * 实体类[SysGroup]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysGroup sysGroup,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_group");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroup,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysGroup sysGroup,java.lang.Long sysGroupId) {
        if (sysGroupId > 0L) {
            sysGroup.setSysGroupId(sysGroupId);
        } else {
            sysGroup.setSysGroupId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_group(");
        List<Object> list = new ArrayList<>();
        if (sysGroup.getSysGroupId() != null && !sysGroup.getSysGroupId().toString().isEmpty()) {
            sb.append("sys_group_id,");
            list.add(sysGroup.getSysGroupId());
        }
        if (sysGroup.getSysGroupName() != null && !sysGroup.getSysGroupName().toString().isEmpty()) {
            sb.append("sys_group_name,");
            list.add(sysGroup.getSysGroupName());
        }
        if (sysGroup.getSysGroupType() != null && !sysGroup.getSysGroupType().toString().isEmpty()) {
            sb.append("sys_group_type,");
            list.add(sysGroup.getSysGroupType());
        }
        if (sysGroup.getSysGroupDesc() != null && !sysGroup.getSysGroupDesc().toString().isEmpty()) {
            sb.append("sys_group_desc,");
            list.add(sysGroup.getSysGroupDesc());
        }
        if (sysGroup.getSysGroupSup() != null && !sysGroup.getSysGroupSup().toString().isEmpty()) {
            sb.append("sys_group_sup,");
            list.add(sysGroup.getSysGroupSup());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysGroup sysGroup) {
        if (sysGroup.getSysGroupId() == null || sysGroup.getSysGroupId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_group set");
        List<Object> list = new ArrayList<>();
        if (sysGroup.getSysGroupName() != null && !sysGroup.getSysGroupName().toString().isEmpty()) {
            sb.append(" sys_group_name = ?,");
            list.add(sysGroup.getSysGroupName());
        }
        if (sysGroup.getSysGroupType() != null && !sysGroup.getSysGroupType().toString().isEmpty()) {
            sb.append(" sys_group_type = ?,");
            list.add(sysGroup.getSysGroupType());
        }
        if (sysGroup.getSysGroupDesc() != null && !sysGroup.getSysGroupDesc().toString().isEmpty()) {
            sb.append(" sys_group_desc = ?,");
            list.add(sysGroup.getSysGroupDesc());
        }
        if (sysGroup.getSysGroupSup() != null && !sysGroup.getSysGroupSup().toString().isEmpty()) {
            sb.append(" sys_group_sup = ?,");
            list.add(sysGroup.getSysGroupSup());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_group_id = ?");
        list.add(sysGroup.getSysGroupId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysGroup sysGroup,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_group");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroup,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysGroup sysGroup,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_group_id", sysGroup.getSysGroupId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_group_id", sysGroup.getSysGroupId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_name", sysGroup.getSysGroupName(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_type", sysGroup.getSysGroupType(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_desc", sysGroup.getSysGroupDesc(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_sup", sysGroup.getSysGroupSup(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysGroup asSysGroup(Map<String,String[]> requestData){
        List<SysGroup> list=asListSysGroup(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysGroup> asListSysGroup(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysGroupIdArray = requestData.get("sysGroupId");
        if (sysGroupIdArray!=null && sysGroupIdArray.length>0) {
            if (size<sysGroupIdArray.length) {
                size=sysGroupIdArray.length;
            }
        }
        String[] sysGroupNameArray = requestData.get("sysGroupName");
        if (sysGroupNameArray!=null && sysGroupNameArray.length>0) {
            if (size<sysGroupNameArray.length) {
                size=sysGroupNameArray.length;
            }
        }
        String[] sysGroupTypeArray = requestData.get("sysGroupType");
        if (sysGroupTypeArray!=null && sysGroupTypeArray.length>0) {
            if (size<sysGroupTypeArray.length) {
                size=sysGroupTypeArray.length;
            }
        }
        String[] sysGroupDescArray = requestData.get("sysGroupDesc");
        if (sysGroupDescArray!=null && sysGroupDescArray.length>0) {
            if (size<sysGroupDescArray.length) {
                size=sysGroupDescArray.length;
            }
        }
        String[] sysGroupSupArray = requestData.get("sysGroupSup");
        if (sysGroupSupArray!=null && sysGroupSupArray.length>0) {
            if (size<sysGroupSupArray.length) {
                size=sysGroupSupArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysGroup> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysGroup sysGroup=new SysGroup();
            if (sysGroupIdArray!=null && i <= sysGroupIdArray.length-1) {
                sysGroup.setSysGroupId(java.lang.Long.valueOf(sysGroupIdArray[i]));
            }
            if (sysGroupNameArray!=null && i <= sysGroupNameArray.length-1) {
                sysGroup.setSysGroupName(java.lang.String.valueOf(sysGroupNameArray[i]));
            }
            if (sysGroupTypeArray!=null && i <= sysGroupTypeArray.length-1) {
                sysGroup.setSysGroupType(java.lang.Long.valueOf(sysGroupTypeArray[i]));
            }
            if (sysGroupDescArray!=null && i <= sysGroupDescArray.length-1) {
                sysGroup.setSysGroupDesc(java.lang.String.valueOf(sysGroupDescArray[i]));
            }
            if (sysGroupSupArray!=null && i <= sysGroupSupArray.length-1) {
                sysGroup.setSysGroupSup(java.lang.Long.valueOf(sysGroupSupArray[i]));
            }
            list.add(sysGroup);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysGroupColumn sysGroupColumn) {
        return toSelectSql(sysGroupColumn,"sys_group_column_id", "asc", 0, false);
    }
    /**
     * 实体类[SysGroupColumn]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysGroupColumn sysGroupColumn,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_group_column");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroupColumn,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysGroupColumn sysGroupColumn,java.lang.Long sysGroupColumnId) {
        if (sysGroupColumnId > 0L) {
            sysGroupColumn.setSysGroupColumnId(sysGroupColumnId);
        } else {
            sysGroupColumn.setSysGroupColumnId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_group_column(");
        List<Object> list = new ArrayList<>();
        if (sysGroupColumn.getSysGroupColumnId() != null && !sysGroupColumn.getSysGroupColumnId().toString().isEmpty()) {
            sb.append("sys_group_column_id,");
            list.add(sysGroupColumn.getSysGroupColumnId());
        }
        if (sysGroupColumn.getSysGroupColumnKey() != null && !sysGroupColumn.getSysGroupColumnKey().toString().isEmpty()) {
            sb.append("sys_group_column_key,");
            list.add(sysGroupColumn.getSysGroupColumnKey());
        }
        if (sysGroupColumn.getSysGroupColumnName() != null && !sysGroupColumn.getSysGroupColumnName().toString().isEmpty()) {
            sb.append("sys_group_column_name,");
            list.add(sysGroupColumn.getSysGroupColumnName());
        }
        if (sysGroupColumn.getSysGroupColumnTable() != null && !sysGroupColumn.getSysGroupColumnTable().toString().isEmpty()) {
            sb.append("sys_group_column_table,");
            list.add(sysGroupColumn.getSysGroupColumnTable());
        }
        if (sysGroupColumn.getSysGroupColumnEdit() != null && !sysGroupColumn.getSysGroupColumnEdit().toString().isEmpty()) {
            sb.append("sys_group_column_edit,");
            list.add(sysGroupColumn.getSysGroupColumnEdit());
        }
        if (sysGroupColumn.getSysGroupColumnUpdate() != null && !sysGroupColumn.getSysGroupColumnUpdate().toString().isEmpty()) {
            sb.append("sys_group_column_update,");
            list.add(sysGroupColumn.getSysGroupColumnUpdate());
        }
        if (sysGroupColumn.getSysGroupColumnSave() != null && !sysGroupColumn.getSysGroupColumnSave().toString().isEmpty()) {
            sb.append("sys_group_column_save,");
            list.add(sysGroupColumn.getSysGroupColumnSave());
        }
        if (sysGroupColumn.getSysGroupColumnQuery() != null && !sysGroupColumn.getSysGroupColumnQuery().toString().isEmpty()) {
            sb.append("sys_group_column_query,");
            list.add(sysGroupColumn.getSysGroupColumnQuery());
        }
        if (sysGroupColumn.getSysGroupColumnSaveDef() != null && !sysGroupColumn.getSysGroupColumnSaveDef().toString().isEmpty()) {
            sb.append("sys_group_column_save_def,");
            list.add(sysGroupColumn.getSysGroupColumnSaveDef());
        }
        if (sysGroupColumn.getSysGroupColumnUpdateDef() != null && !sysGroupColumn.getSysGroupColumnUpdateDef().toString().isEmpty()) {
            sb.append("sys_group_column_update_def,");
            list.add(sysGroupColumn.getSysGroupColumnUpdateDef());
        }
        if (sysGroupColumn.getSysGroupColumnQuerySymbol() != null && !sysGroupColumn.getSysGroupColumnQuerySymbol().toString().isEmpty()) {
            sb.append("sys_group_column_query_symbol,");
            list.add(sysGroupColumn.getSysGroupColumnQuerySymbol());
        }
        if (sysGroupColumn.getSysGroupColumnQueryMontage() != null && !sysGroupColumn.getSysGroupColumnQueryMontage().toString().isEmpty()) {
            sb.append("sys_group_column_query_montage,");
            list.add(sysGroupColumn.getSysGroupColumnQueryMontage());
        }
        if (sysGroupColumn.getSysGroupColumnQueryDef() != null && !sysGroupColumn.getSysGroupColumnQueryDef().toString().isEmpty()) {
            sb.append("sys_group_column_query_def,");
            list.add(sysGroupColumn.getSysGroupColumnQueryDef());
        }
        if (sysGroupColumn.getSysGroupColumnGid() != null && !sysGroupColumn.getSysGroupColumnGid().toString().isEmpty()) {
            sb.append("sys_group_column_gid,");
            list.add(sysGroupColumn.getSysGroupColumnGid());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysGroupColumn sysGroupColumn) {
        if (sysGroupColumn.getSysGroupColumnId() == null || sysGroupColumn.getSysGroupColumnId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_group_column set");
        List<Object> list = new ArrayList<>();
        if (sysGroupColumn.getSysGroupColumnKey() != null && !sysGroupColumn.getSysGroupColumnKey().toString().isEmpty()) {
            sb.append(" sys_group_column_key = ?,");
            list.add(sysGroupColumn.getSysGroupColumnKey());
        }
        if (sysGroupColumn.getSysGroupColumnName() != null && !sysGroupColumn.getSysGroupColumnName().toString().isEmpty()) {
            sb.append(" sys_group_column_name = ?,");
            list.add(sysGroupColumn.getSysGroupColumnName());
        }
        if (sysGroupColumn.getSysGroupColumnTable() != null && !sysGroupColumn.getSysGroupColumnTable().toString().isEmpty()) {
            sb.append(" sys_group_column_table = ?,");
            list.add(sysGroupColumn.getSysGroupColumnTable());
        }
        if (sysGroupColumn.getSysGroupColumnEdit() != null && !sysGroupColumn.getSysGroupColumnEdit().toString().isEmpty()) {
            sb.append(" sys_group_column_edit = ?,");
            list.add(sysGroupColumn.getSysGroupColumnEdit());
        }
        if (sysGroupColumn.getSysGroupColumnUpdate() != null && !sysGroupColumn.getSysGroupColumnUpdate().toString().isEmpty()) {
            sb.append(" sys_group_column_update = ?,");
            list.add(sysGroupColumn.getSysGroupColumnUpdate());
        }
        if (sysGroupColumn.getSysGroupColumnSave() != null && !sysGroupColumn.getSysGroupColumnSave().toString().isEmpty()) {
            sb.append(" sys_group_column_save = ?,");
            list.add(sysGroupColumn.getSysGroupColumnSave());
        }
        if (sysGroupColumn.getSysGroupColumnQuery() != null && !sysGroupColumn.getSysGroupColumnQuery().toString().isEmpty()) {
            sb.append(" sys_group_column_query = ?,");
            list.add(sysGroupColumn.getSysGroupColumnQuery());
        }
        if (sysGroupColumn.getSysGroupColumnSaveDef() != null && !sysGroupColumn.getSysGroupColumnSaveDef().toString().isEmpty()) {
            sb.append(" sys_group_column_save_def = ?,");
            list.add(sysGroupColumn.getSysGroupColumnSaveDef());
        }
        if (sysGroupColumn.getSysGroupColumnUpdateDef() != null && !sysGroupColumn.getSysGroupColumnUpdateDef().toString().isEmpty()) {
            sb.append(" sys_group_column_update_def = ?,");
            list.add(sysGroupColumn.getSysGroupColumnUpdateDef());
        }
        if (sysGroupColumn.getSysGroupColumnQuerySymbol() != null && !sysGroupColumn.getSysGroupColumnQuerySymbol().toString().isEmpty()) {
            sb.append(" sys_group_column_query_symbol = ?,");
            list.add(sysGroupColumn.getSysGroupColumnQuerySymbol());
        }
        if (sysGroupColumn.getSysGroupColumnQueryMontage() != null && !sysGroupColumn.getSysGroupColumnQueryMontage().toString().isEmpty()) {
            sb.append(" sys_group_column_query_montage = ?,");
            list.add(sysGroupColumn.getSysGroupColumnQueryMontage());
        }
        if (sysGroupColumn.getSysGroupColumnQueryDef() != null && !sysGroupColumn.getSysGroupColumnQueryDef().toString().isEmpty()) {
            sb.append(" sys_group_column_query_def = ?,");
            list.add(sysGroupColumn.getSysGroupColumnQueryDef());
        }
        if (sysGroupColumn.getSysGroupColumnGid() != null && !sysGroupColumn.getSysGroupColumnGid().toString().isEmpty()) {
            sb.append(" sys_group_column_gid = ?,");
            list.add(sysGroupColumn.getSysGroupColumnGid());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_group_column_id = ?");
        list.add(sysGroupColumn.getSysGroupColumnId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysGroupColumn sysGroupColumn,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_group_column");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroupColumn,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysGroupColumn sysGroupColumn,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_group_column_id", sysGroupColumn.getSysGroupColumnId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_group_column_id", sysGroupColumn.getSysGroupColumnId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_key", sysGroupColumn.getSysGroupColumnKey(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_name", sysGroupColumn.getSysGroupColumnName(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_table", sysGroupColumn.getSysGroupColumnTable(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_edit", sysGroupColumn.getSysGroupColumnEdit(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_update", sysGroupColumn.getSysGroupColumnUpdate(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_save", sysGroupColumn.getSysGroupColumnSave(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_query", sysGroupColumn.getSysGroupColumnQuery(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_save_def", sysGroupColumn.getSysGroupColumnSaveDef(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_update_def", sysGroupColumn.getSysGroupColumnUpdateDef(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_query_symbol", sysGroupColumn.getSysGroupColumnQuerySymbol(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_query_montage", sysGroupColumn.getSysGroupColumnQueryMontage(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_query_def", sysGroupColumn.getSysGroupColumnQueryDef(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_column_gid", sysGroupColumn.getSysGroupColumnGid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysGroupColumn asSysGroupColumn(Map<String,String[]> requestData){
        List<SysGroupColumn> list=asListSysGroupColumn(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysGroupColumn> asListSysGroupColumn(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysGroupColumnIdArray = requestData.get("sysGroupColumnId");
        if (sysGroupColumnIdArray!=null && sysGroupColumnIdArray.length>0) {
            if (size<sysGroupColumnIdArray.length) {
                size=sysGroupColumnIdArray.length;
            }
        }
        String[] sysGroupColumnKeyArray = requestData.get("sysGroupColumnKey");
        if (sysGroupColumnKeyArray!=null && sysGroupColumnKeyArray.length>0) {
            if (size<sysGroupColumnKeyArray.length) {
                size=sysGroupColumnKeyArray.length;
            }
        }
        String[] sysGroupColumnNameArray = requestData.get("sysGroupColumnName");
        if (sysGroupColumnNameArray!=null && sysGroupColumnNameArray.length>0) {
            if (size<sysGroupColumnNameArray.length) {
                size=sysGroupColumnNameArray.length;
            }
        }
        String[] sysGroupColumnTableArray = requestData.get("sysGroupColumnTable");
        if (sysGroupColumnTableArray!=null && sysGroupColumnTableArray.length>0) {
            if (size<sysGroupColumnTableArray.length) {
                size=sysGroupColumnTableArray.length;
            }
        }
        String[] sysGroupColumnEditArray = requestData.get("sysGroupColumnEdit");
        if (sysGroupColumnEditArray!=null && sysGroupColumnEditArray.length>0) {
            if (size<sysGroupColumnEditArray.length) {
                size=sysGroupColumnEditArray.length;
            }
        }
        String[] sysGroupColumnUpdateArray = requestData.get("sysGroupColumnUpdate");
        if (sysGroupColumnUpdateArray!=null && sysGroupColumnUpdateArray.length>0) {
            if (size<sysGroupColumnUpdateArray.length) {
                size=sysGroupColumnUpdateArray.length;
            }
        }
        String[] sysGroupColumnSaveArray = requestData.get("sysGroupColumnSave");
        if (sysGroupColumnSaveArray!=null && sysGroupColumnSaveArray.length>0) {
            if (size<sysGroupColumnSaveArray.length) {
                size=sysGroupColumnSaveArray.length;
            }
        }
        String[] sysGroupColumnQueryArray = requestData.get("sysGroupColumnQuery");
        if (sysGroupColumnQueryArray!=null && sysGroupColumnQueryArray.length>0) {
            if (size<sysGroupColumnQueryArray.length) {
                size=sysGroupColumnQueryArray.length;
            }
        }
        String[] sysGroupColumnSaveDefArray = requestData.get("sysGroupColumnSaveDef");
        if (sysGroupColumnSaveDefArray!=null && sysGroupColumnSaveDefArray.length>0) {
            if (size<sysGroupColumnSaveDefArray.length) {
                size=sysGroupColumnSaveDefArray.length;
            }
        }
        String[] sysGroupColumnUpdateDefArray = requestData.get("sysGroupColumnUpdateDef");
        if (sysGroupColumnUpdateDefArray!=null && sysGroupColumnUpdateDefArray.length>0) {
            if (size<sysGroupColumnUpdateDefArray.length) {
                size=sysGroupColumnUpdateDefArray.length;
            }
        }
        String[] sysGroupColumnQuerySymbolArray = requestData.get("sysGroupColumnQuerySymbol");
        if (sysGroupColumnQuerySymbolArray!=null && sysGroupColumnQuerySymbolArray.length>0) {
            if (size<sysGroupColumnQuerySymbolArray.length) {
                size=sysGroupColumnQuerySymbolArray.length;
            }
        }
        String[] sysGroupColumnQueryMontageArray = requestData.get("sysGroupColumnQueryMontage");
        if (sysGroupColumnQueryMontageArray!=null && sysGroupColumnQueryMontageArray.length>0) {
            if (size<sysGroupColumnQueryMontageArray.length) {
                size=sysGroupColumnQueryMontageArray.length;
            }
        }
        String[] sysGroupColumnQueryDefArray = requestData.get("sysGroupColumnQueryDef");
        if (sysGroupColumnQueryDefArray!=null && sysGroupColumnQueryDefArray.length>0) {
            if (size<sysGroupColumnQueryDefArray.length) {
                size=sysGroupColumnQueryDefArray.length;
            }
        }
        String[] sysGroupColumnGidArray = requestData.get("sysGroupColumnGid");
        if (sysGroupColumnGidArray!=null && sysGroupColumnGidArray.length>0) {
            if (size<sysGroupColumnGidArray.length) {
                size=sysGroupColumnGidArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysGroupColumn> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysGroupColumn sysGroupColumn=new SysGroupColumn();
            if (sysGroupColumnIdArray!=null && i <= sysGroupColumnIdArray.length-1) {
                sysGroupColumn.setSysGroupColumnId(java.lang.Long.valueOf(sysGroupColumnIdArray[i]));
            }
            if (sysGroupColumnKeyArray!=null && i <= sysGroupColumnKeyArray.length-1) {
                sysGroupColumn.setSysGroupColumnKey(java.lang.String.valueOf(sysGroupColumnKeyArray[i]));
            }
            if (sysGroupColumnNameArray!=null && i <= sysGroupColumnNameArray.length-1) {
                sysGroupColumn.setSysGroupColumnName(java.lang.String.valueOf(sysGroupColumnNameArray[i]));
            }
            if (sysGroupColumnTableArray!=null && i <= sysGroupColumnTableArray.length-1) {
                sysGroupColumn.setSysGroupColumnTable(java.lang.Long.valueOf(sysGroupColumnTableArray[i]));
            }
            if (sysGroupColumnEditArray!=null && i <= sysGroupColumnEditArray.length-1) {
                sysGroupColumn.setSysGroupColumnEdit(java.lang.Long.valueOf(sysGroupColumnEditArray[i]));
            }
            if (sysGroupColumnUpdateArray!=null && i <= sysGroupColumnUpdateArray.length-1) {
                sysGroupColumn.setSysGroupColumnUpdate(java.lang.Long.valueOf(sysGroupColumnUpdateArray[i]));
            }
            if (sysGroupColumnSaveArray!=null && i <= sysGroupColumnSaveArray.length-1) {
                sysGroupColumn.setSysGroupColumnSave(java.lang.Long.valueOf(sysGroupColumnSaveArray[i]));
            }
            if (sysGroupColumnQueryArray!=null && i <= sysGroupColumnQueryArray.length-1) {
                sysGroupColumn.setSysGroupColumnQuery(java.lang.Long.valueOf(sysGroupColumnQueryArray[i]));
            }
            if (sysGroupColumnSaveDefArray!=null && i <= sysGroupColumnSaveDefArray.length-1) {
                sysGroupColumn.setSysGroupColumnSaveDef(java.lang.String.valueOf(sysGroupColumnSaveDefArray[i]));
            }
            if (sysGroupColumnUpdateDefArray!=null && i <= sysGroupColumnUpdateDefArray.length-1) {
                sysGroupColumn.setSysGroupColumnUpdateDef(java.lang.String.valueOf(sysGroupColumnUpdateDefArray[i]));
            }
            if (sysGroupColumnQuerySymbolArray!=null && i <= sysGroupColumnQuerySymbolArray.length-1) {
                sysGroupColumn.setSysGroupColumnQuerySymbol(java.lang.Long.valueOf(sysGroupColumnQuerySymbolArray[i]));
            }
            if (sysGroupColumnQueryMontageArray!=null && i <= sysGroupColumnQueryMontageArray.length-1) {
                sysGroupColumn.setSysGroupColumnQueryMontage(java.lang.Long.valueOf(sysGroupColumnQueryMontageArray[i]));
            }
            if (sysGroupColumnQueryDefArray!=null && i <= sysGroupColumnQueryDefArray.length-1) {
                sysGroupColumn.setSysGroupColumnQueryDef(java.lang.String.valueOf(sysGroupColumnQueryDefArray[i]));
            }
            if (sysGroupColumnGidArray!=null && i <= sysGroupColumnGidArray.length-1) {
                sysGroupColumn.setSysGroupColumnGid(java.lang.Long.valueOf(sysGroupColumnGidArray[i]));
            }
            list.add(sysGroupColumn);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysGroupPermission sysGroupPermission) {
        return toSelectSql(sysGroupPermission,"sys_group_permission_id", "asc", 0, false);
    }
    /**
     * 实体类[SysGroupPermission]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysGroupPermission sysGroupPermission,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_group_permission");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroupPermission,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysGroupPermission sysGroupPermission,java.lang.Long sysGroupPermissionId) {
        if (sysGroupPermissionId > 0L) {
            sysGroupPermission.setSysGroupPermissionId(sysGroupPermissionId);
        } else {
            sysGroupPermission.setSysGroupPermissionId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_group_permission(");
        List<Object> list = new ArrayList<>();
        if (sysGroupPermission.getSysGroupPermissionId() != null && !sysGroupPermission.getSysGroupPermissionId().toString().isEmpty()) {
            sb.append("sys_group_permission_id,");
            list.add(sysGroupPermission.getSysGroupPermissionId());
        }
        if (sysGroupPermission.getSysGroupPermissionPid() != null && !sysGroupPermission.getSysGroupPermissionPid().toString().isEmpty()) {
            sb.append("sys_group_permission_pid,");
            list.add(sysGroupPermission.getSysGroupPermissionPid());
        }
        if (sysGroupPermission.getSysGroupPermissionGid() != null && !sysGroupPermission.getSysGroupPermissionGid().toString().isEmpty()) {
            sb.append("sys_group_permission_gid,");
            list.add(sysGroupPermission.getSysGroupPermissionGid());
        }
        if (sysGroupPermission.getSysGroupPermissionTime() != null && !sysGroupPermission.getSysGroupPermissionTime().toString().isEmpty()) {
            sb.append("sys_group_permission_time,");
            list.add(sysGroupPermission.getSysGroupPermissionTime());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysGroupPermission sysGroupPermission) {
        if (sysGroupPermission.getSysGroupPermissionId() == null || sysGroupPermission.getSysGroupPermissionId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_group_permission set");
        List<Object> list = new ArrayList<>();
        if (sysGroupPermission.getSysGroupPermissionPid() != null && !sysGroupPermission.getSysGroupPermissionPid().toString().isEmpty()) {
            sb.append(" sys_group_permission_pid = ?,");
            list.add(sysGroupPermission.getSysGroupPermissionPid());
        }
        if (sysGroupPermission.getSysGroupPermissionGid() != null && !sysGroupPermission.getSysGroupPermissionGid().toString().isEmpty()) {
            sb.append(" sys_group_permission_gid = ?,");
            list.add(sysGroupPermission.getSysGroupPermissionGid());
        }
        if (sysGroupPermission.getSysGroupPermissionTime() != null && !sysGroupPermission.getSysGroupPermissionTime().toString().isEmpty()) {
            sb.append(" sys_group_permission_time = ?,");
            list.add(sysGroupPermission.getSysGroupPermissionTime());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_group_permission_id = ?");
        list.add(sysGroupPermission.getSysGroupPermissionId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysGroupPermission sysGroupPermission,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_group_permission");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroupPermission,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysGroupPermission sysGroupPermission,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_group_permission_id", sysGroupPermission.getSysGroupPermissionId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_group_permission_id", sysGroupPermission.getSysGroupPermissionId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_permission_pid", sysGroupPermission.getSysGroupPermissionPid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_permission_gid", sysGroupPermission.getSysGroupPermissionGid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_permission_time", sysGroupPermission.getSysGroupPermissionTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysGroupPermission asSysGroupPermission(Map<String,String[]> requestData){
        List<SysGroupPermission> list=asListSysGroupPermission(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysGroupPermission> asListSysGroupPermission(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysGroupPermissionIdArray = requestData.get("sysGroupPermissionId");
        if (sysGroupPermissionIdArray!=null && sysGroupPermissionIdArray.length>0) {
            if (size<sysGroupPermissionIdArray.length) {
                size=sysGroupPermissionIdArray.length;
            }
        }
        String[] sysGroupPermissionPidArray = requestData.get("sysGroupPermissionPid");
        if (sysGroupPermissionPidArray!=null && sysGroupPermissionPidArray.length>0) {
            if (size<sysGroupPermissionPidArray.length) {
                size=sysGroupPermissionPidArray.length;
            }
        }
        String[] sysGroupPermissionGidArray = requestData.get("sysGroupPermissionGid");
        if (sysGroupPermissionGidArray!=null && sysGroupPermissionGidArray.length>0) {
            if (size<sysGroupPermissionGidArray.length) {
                size=sysGroupPermissionGidArray.length;
            }
        }
        String[] sysGroupPermissionTimeArray = requestData.get("sysGroupPermissionTime");
        if (sysGroupPermissionTimeArray!=null && sysGroupPermissionTimeArray.length>0) {
            if (size<sysGroupPermissionTimeArray.length) {
                size=sysGroupPermissionTimeArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysGroupPermission> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysGroupPermission sysGroupPermission=new SysGroupPermission();
            if (sysGroupPermissionIdArray!=null && i <= sysGroupPermissionIdArray.length-1) {
                sysGroupPermission.setSysGroupPermissionId(java.lang.Long.valueOf(sysGroupPermissionIdArray[i]));
            }
            if (sysGroupPermissionPidArray!=null && i <= sysGroupPermissionPidArray.length-1) {
                sysGroupPermission.setSysGroupPermissionPid(java.lang.Long.valueOf(sysGroupPermissionPidArray[i]));
            }
            if (sysGroupPermissionGidArray!=null && i <= sysGroupPermissionGidArray.length-1) {
                sysGroupPermission.setSysGroupPermissionGid(java.lang.Long.valueOf(sysGroupPermissionGidArray[i]));
            }
            if (sysGroupPermissionTimeArray!=null && i <= sysGroupPermissionTimeArray.length-1) {
                sysGroupPermission.setSysGroupPermissionTime(java.lang.String.valueOf(sysGroupPermissionTimeArray[i]));
            }
            list.add(sysGroupPermission);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysGroupTable sysGroupTable) {
        return toSelectSql(sysGroupTable,"sys_group_table_id", "asc", 0, false);
    }
    /**
     * 实体类[SysGroupTable]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysGroupTable sysGroupTable,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_group_table");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroupTable,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysGroupTable sysGroupTable,java.lang.Long sysGroupTableId) {
        if (sysGroupTableId > 0L) {
            sysGroupTable.setSysGroupTableId(sysGroupTableId);
        } else {
            sysGroupTable.setSysGroupTableId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_group_table(");
        List<Object> list = new ArrayList<>();
        if (sysGroupTable.getSysGroupTableId() != null && !sysGroupTable.getSysGroupTableId().toString().isEmpty()) {
            sb.append("sys_group_table_id,");
            list.add(sysGroupTable.getSysGroupTableId());
        }
        if (sysGroupTable.getSysGroupTableKey() != null && !sysGroupTable.getSysGroupTableKey().toString().isEmpty()) {
            sb.append("sys_group_table_key,");
            list.add(sysGroupTable.getSysGroupTableKey());
        }
        if (sysGroupTable.getSysGroupTableGid() != null && !sysGroupTable.getSysGroupTableGid().toString().isEmpty()) {
            sb.append("sys_group_table_gid,");
            list.add(sysGroupTable.getSysGroupTableGid());
        }
        if (sysGroupTable.getSysGroupTableButSaveOpen() != null && !sysGroupTable.getSysGroupTableButSaveOpen().toString().isEmpty()) {
            sb.append("sys_group_table_but_save_open,");
            list.add(sysGroupTable.getSysGroupTableButSaveOpen());
        }
        if (sysGroupTable.getSysGroupTableButDeleteOpen() != null && !sysGroupTable.getSysGroupTableButDeleteOpen().toString().isEmpty()) {
            sb.append("sys_group_table_but_delete_open,");
            list.add(sysGroupTable.getSysGroupTableButDeleteOpen());
        }
        if (sysGroupTable.getSysGroupTableTableUpdateOpen() != null && !sysGroupTable.getSysGroupTableTableUpdateOpen().toString().isEmpty()) {
            sb.append("sys_group_table_table_update_open,");
            list.add(sysGroupTable.getSysGroupTableTableUpdateOpen());
        }
        if (sysGroupTable.getSysGroupTableTableDeleteOpen() != null && !sysGroupTable.getSysGroupTableTableDeleteOpen().toString().isEmpty()) {
            sb.append("sys_group_table_table_delete_open,");
            list.add(sysGroupTable.getSysGroupTableTableDeleteOpen());
        }
        if (sysGroupTable.getSysGroupTableButQueryOpen() != null && !sysGroupTable.getSysGroupTableButQueryOpen().toString().isEmpty()) {
            sb.append("sys_group_table_but_query_open,");
            list.add(sysGroupTable.getSysGroupTableButQueryOpen());
        }
        if (sysGroupTable.getSysGroupTableTableButWidth() != null && !sysGroupTable.getSysGroupTableTableButWidth().toString().isEmpty()) {
            sb.append("sys_group_table_table_but_width,");
            list.add(sysGroupTable.getSysGroupTableTableButWidth());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysGroupTable sysGroupTable) {
        if (sysGroupTable.getSysGroupTableId() == null || sysGroupTable.getSysGroupTableId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_group_table set");
        List<Object> list = new ArrayList<>();
        if (sysGroupTable.getSysGroupTableKey() != null && !sysGroupTable.getSysGroupTableKey().toString().isEmpty()) {
            sb.append(" sys_group_table_key = ?,");
            list.add(sysGroupTable.getSysGroupTableKey());
        }
        if (sysGroupTable.getSysGroupTableGid() != null && !sysGroupTable.getSysGroupTableGid().toString().isEmpty()) {
            sb.append(" sys_group_table_gid = ?,");
            list.add(sysGroupTable.getSysGroupTableGid());
        }
        if (sysGroupTable.getSysGroupTableButSaveOpen() != null && !sysGroupTable.getSysGroupTableButSaveOpen().toString().isEmpty()) {
            sb.append(" sys_group_table_but_save_open = ?,");
            list.add(sysGroupTable.getSysGroupTableButSaveOpen());
        }
        if (sysGroupTable.getSysGroupTableButDeleteOpen() != null && !sysGroupTable.getSysGroupTableButDeleteOpen().toString().isEmpty()) {
            sb.append(" sys_group_table_but_delete_open = ?,");
            list.add(sysGroupTable.getSysGroupTableButDeleteOpen());
        }
        if (sysGroupTable.getSysGroupTableTableUpdateOpen() != null && !sysGroupTable.getSysGroupTableTableUpdateOpen().toString().isEmpty()) {
            sb.append(" sys_group_table_table_update_open = ?,");
            list.add(sysGroupTable.getSysGroupTableTableUpdateOpen());
        }
        if (sysGroupTable.getSysGroupTableTableDeleteOpen() != null && !sysGroupTable.getSysGroupTableTableDeleteOpen().toString().isEmpty()) {
            sb.append(" sys_group_table_table_delete_open = ?,");
            list.add(sysGroupTable.getSysGroupTableTableDeleteOpen());
        }
        if (sysGroupTable.getSysGroupTableButQueryOpen() != null && !sysGroupTable.getSysGroupTableButQueryOpen().toString().isEmpty()) {
            sb.append(" sys_group_table_but_query_open = ?,");
            list.add(sysGroupTable.getSysGroupTableButQueryOpen());
        }
        if (sysGroupTable.getSysGroupTableTableButWidth() != null && !sysGroupTable.getSysGroupTableTableButWidth().toString().isEmpty()) {
            sb.append(" sys_group_table_table_but_width = ?,");
            list.add(sysGroupTable.getSysGroupTableTableButWidth());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_group_table_id = ?");
        list.add(sysGroupTable.getSysGroupTableId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysGroupTable sysGroupTable,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_group_table");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysGroupTable,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysGroupTable sysGroupTable,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_group_table_id", sysGroupTable.getSysGroupTableId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_group_table_id", sysGroupTable.getSysGroupTableId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_key", sysGroupTable.getSysGroupTableKey(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_gid", sysGroupTable.getSysGroupTableGid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_but_save_open", sysGroupTable.getSysGroupTableButSaveOpen(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_but_delete_open", sysGroupTable.getSysGroupTableButDeleteOpen(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_table_update_open", sysGroupTable.getSysGroupTableTableUpdateOpen(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_table_delete_open", sysGroupTable.getSysGroupTableTableDeleteOpen(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_but_query_open", sysGroupTable.getSysGroupTableButQueryOpen(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_group_table_table_but_width", sysGroupTable.getSysGroupTableTableButWidth(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysGroupTable asSysGroupTable(Map<String,String[]> requestData){
        List<SysGroupTable> list=asListSysGroupTable(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysGroupTable> asListSysGroupTable(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysGroupTableIdArray = requestData.get("sysGroupTableId");
        if (sysGroupTableIdArray!=null && sysGroupTableIdArray.length>0) {
            if (size<sysGroupTableIdArray.length) {
                size=sysGroupTableIdArray.length;
            }
        }
        String[] sysGroupTableKeyArray = requestData.get("sysGroupTableKey");
        if (sysGroupTableKeyArray!=null && sysGroupTableKeyArray.length>0) {
            if (size<sysGroupTableKeyArray.length) {
                size=sysGroupTableKeyArray.length;
            }
        }
        String[] sysGroupTableGidArray = requestData.get("sysGroupTableGid");
        if (sysGroupTableGidArray!=null && sysGroupTableGidArray.length>0) {
            if (size<sysGroupTableGidArray.length) {
                size=sysGroupTableGidArray.length;
            }
        }
        String[] sysGroupTableButSaveOpenArray = requestData.get("sysGroupTableButSaveOpen");
        if (sysGroupTableButSaveOpenArray!=null && sysGroupTableButSaveOpenArray.length>0) {
            if (size<sysGroupTableButSaveOpenArray.length) {
                size=sysGroupTableButSaveOpenArray.length;
            }
        }
        String[] sysGroupTableButDeleteOpenArray = requestData.get("sysGroupTableButDeleteOpen");
        if (sysGroupTableButDeleteOpenArray!=null && sysGroupTableButDeleteOpenArray.length>0) {
            if (size<sysGroupTableButDeleteOpenArray.length) {
                size=sysGroupTableButDeleteOpenArray.length;
            }
        }
        String[] sysGroupTableTableUpdateOpenArray = requestData.get("sysGroupTableTableUpdateOpen");
        if (sysGroupTableTableUpdateOpenArray!=null && sysGroupTableTableUpdateOpenArray.length>0) {
            if (size<sysGroupTableTableUpdateOpenArray.length) {
                size=sysGroupTableTableUpdateOpenArray.length;
            }
        }
        String[] sysGroupTableTableDeleteOpenArray = requestData.get("sysGroupTableTableDeleteOpen");
        if (sysGroupTableTableDeleteOpenArray!=null && sysGroupTableTableDeleteOpenArray.length>0) {
            if (size<sysGroupTableTableDeleteOpenArray.length) {
                size=sysGroupTableTableDeleteOpenArray.length;
            }
        }
        String[] sysGroupTableButQueryOpenArray = requestData.get("sysGroupTableButQueryOpen");
        if (sysGroupTableButQueryOpenArray!=null && sysGroupTableButQueryOpenArray.length>0) {
            if (size<sysGroupTableButQueryOpenArray.length) {
                size=sysGroupTableButQueryOpenArray.length;
            }
        }
        String[] sysGroupTableTableButWidthArray = requestData.get("sysGroupTableTableButWidth");
        if (sysGroupTableTableButWidthArray!=null && sysGroupTableTableButWidthArray.length>0) {
            if (size<sysGroupTableTableButWidthArray.length) {
                size=sysGroupTableTableButWidthArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysGroupTable> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysGroupTable sysGroupTable=new SysGroupTable();
            if (sysGroupTableIdArray!=null && i <= sysGroupTableIdArray.length-1) {
                sysGroupTable.setSysGroupTableId(java.lang.Long.valueOf(sysGroupTableIdArray[i]));
            }
            if (sysGroupTableKeyArray!=null && i <= sysGroupTableKeyArray.length-1) {
                sysGroupTable.setSysGroupTableKey(java.lang.String.valueOf(sysGroupTableKeyArray[i]));
            }
            if (sysGroupTableGidArray!=null && i <= sysGroupTableGidArray.length-1) {
                sysGroupTable.setSysGroupTableGid(java.lang.Long.valueOf(sysGroupTableGidArray[i]));
            }
            if (sysGroupTableButSaveOpenArray!=null && i <= sysGroupTableButSaveOpenArray.length-1) {
                sysGroupTable.setSysGroupTableButSaveOpen(java.lang.Long.valueOf(sysGroupTableButSaveOpenArray[i]));
            }
            if (sysGroupTableButDeleteOpenArray!=null && i <= sysGroupTableButDeleteOpenArray.length-1) {
                sysGroupTable.setSysGroupTableButDeleteOpen(java.lang.Long.valueOf(sysGroupTableButDeleteOpenArray[i]));
            }
            if (sysGroupTableTableUpdateOpenArray!=null && i <= sysGroupTableTableUpdateOpenArray.length-1) {
                sysGroupTable.setSysGroupTableTableUpdateOpen(java.lang.Long.valueOf(sysGroupTableTableUpdateOpenArray[i]));
            }
            if (sysGroupTableTableDeleteOpenArray!=null && i <= sysGroupTableTableDeleteOpenArray.length-1) {
                sysGroupTable.setSysGroupTableTableDeleteOpen(java.lang.Long.valueOf(sysGroupTableTableDeleteOpenArray[i]));
            }
            if (sysGroupTableButQueryOpenArray!=null && i <= sysGroupTableButQueryOpenArray.length-1) {
                sysGroupTable.setSysGroupTableButQueryOpen(java.lang.Long.valueOf(sysGroupTableButQueryOpenArray[i]));
            }
            if (sysGroupTableTableButWidthArray!=null && i <= sysGroupTableTableButWidthArray.length-1) {
                sysGroupTable.setSysGroupTableTableButWidth(java.lang.Long.valueOf(sysGroupTableTableButWidthArray[i]));
            }
            list.add(sysGroupTable);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysLog sysLog) {
        return toSelectSql(sysLog,"sys_log_id", "asc", 0, false);
    }
    /**
     * 实体类[SysLog]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysLog sysLog,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_log");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysLog,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysLog sysLog,java.lang.Long sysLogId) {
        if (sysLogId > 0L) {
            sysLog.setSysLogId(sysLogId);
        } else {
            sysLog.setSysLogId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_log(");
        List<Object> list = new ArrayList<>();
        if (sysLog.getSysLogId() != null && !sysLog.getSysLogId().toString().isEmpty()) {
            sb.append("sys_log_id,");
            list.add(sysLog.getSysLogId());
        }
        if (sysLog.getSysLogTime() != null && !sysLog.getSysLogTime().toString().isEmpty()) {
            sb.append("sys_log_time,");
            list.add(sysLog.getSysLogTime());
        }
        if (sysLog.getSysLogMs() != null && !sysLog.getSysLogMs().toString().isEmpty()) {
            sb.append("sys_log_ms,");
            list.add(sysLog.getSysLogMs());
        }
        if (sysLog.getSysLogSql() != null && !sysLog.getSysLogSql().toString().isEmpty()) {
            sb.append("sys_log_sql,");
            list.add(sysLog.getSysLogSql());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysLog sysLog) {
        if (sysLog.getSysLogId() == null || sysLog.getSysLogId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_log set");
        List<Object> list = new ArrayList<>();
        if (sysLog.getSysLogTime() != null && !sysLog.getSysLogTime().toString().isEmpty()) {
            sb.append(" sys_log_time = ?,");
            list.add(sysLog.getSysLogTime());
        }
        if (sysLog.getSysLogMs() != null && !sysLog.getSysLogMs().toString().isEmpty()) {
            sb.append(" sys_log_ms = ?,");
            list.add(sysLog.getSysLogMs());
        }
        if (sysLog.getSysLogSql() != null && !sysLog.getSysLogSql().toString().isEmpty()) {
            sb.append(" sys_log_sql = ?,");
            list.add(sysLog.getSysLogSql());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_log_id = ?");
        list.add(sysLog.getSysLogId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysLog sysLog,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_log");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysLog,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysLog sysLog,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_log_id", sysLog.getSysLogId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_log_id", sysLog.getSysLogId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_log_time", sysLog.getSysLogTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_log_ms", sysLog.getSysLogMs(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_log_sql", sysLog.getSysLogSql(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysLog asSysLog(Map<String,String[]> requestData){
        List<SysLog> list=asListSysLog(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysLog> asListSysLog(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysLogIdArray = requestData.get("sysLogId");
        if (sysLogIdArray!=null && sysLogIdArray.length>0) {
            if (size<sysLogIdArray.length) {
                size=sysLogIdArray.length;
            }
        }
        String[] sysLogTimeArray = requestData.get("sysLogTime");
        if (sysLogTimeArray!=null && sysLogTimeArray.length>0) {
            if (size<sysLogTimeArray.length) {
                size=sysLogTimeArray.length;
            }
        }
        String[] sysLogMsArray = requestData.get("sysLogMs");
        if (sysLogMsArray!=null && sysLogMsArray.length>0) {
            if (size<sysLogMsArray.length) {
                size=sysLogMsArray.length;
            }
        }
        String[] sysLogSqlArray = requestData.get("sysLogSql");
        if (sysLogSqlArray!=null && sysLogSqlArray.length>0) {
            if (size<sysLogSqlArray.length) {
                size=sysLogSqlArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysLog> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysLog sysLog=new SysLog();
            if (sysLogIdArray!=null && i <= sysLogIdArray.length-1) {
                sysLog.setSysLogId(java.lang.Long.valueOf(sysLogIdArray[i]));
            }
            if (sysLogTimeArray!=null && i <= sysLogTimeArray.length-1) {
                sysLog.setSysLogTime(java.lang.String.valueOf(sysLogTimeArray[i]));
            }
            if (sysLogMsArray!=null && i <= sysLogMsArray.length-1) {
                sysLog.setSysLogMs(java.lang.Long.valueOf(sysLogMsArray[i]));
            }
            if (sysLogSqlArray!=null && i <= sysLogSqlArray.length-1) {
                sysLog.setSysLogSql(java.lang.String.valueOf(sysLogSqlArray[i]));
            }
            list.add(sysLog);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysMapping sysMapping) {
        return toSelectSql(sysMapping,"sys_mapping_id", "asc", 0, false);
    }
    /**
     * 实体类[SysMapping]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysMapping sysMapping,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_mapping");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysMapping,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysMapping sysMapping,java.lang.Long sysMappingId) {
        if (sysMappingId > 0L) {
            sysMapping.setSysMappingId(sysMappingId);
        } else {
            sysMapping.setSysMappingId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_mapping(");
        List<Object> list = new ArrayList<>();
        if (sysMapping.getSysMappingId() != null && !sysMapping.getSysMappingId().toString().isEmpty()) {
            sb.append("sys_mapping_id,");
            list.add(sysMapping.getSysMappingId());
        }
        if (sysMapping.getSysMappingKey() != null && !sysMapping.getSysMappingKey().toString().isEmpty()) {
            sb.append("sys_mapping_key,");
            list.add(sysMapping.getSysMappingKey());
        }
        if (sysMapping.getSysMappingTitle() != null && !sysMapping.getSysMappingTitle().toString().isEmpty()) {
            sb.append("sys_mapping_title,");
            list.add(sysMapping.getSysMappingTitle());
        }
        if (sysMapping.getSysMappingVal() != null && !sysMapping.getSysMappingVal().toString().isEmpty()) {
            sb.append("sys_mapping_val,");
            list.add(sysMapping.getSysMappingVal());
        }
        if (sysMapping.getSysMappingTableWidth() != null && !sysMapping.getSysMappingTableWidth().toString().isEmpty()) {
            sb.append("sys_mapping_table_width,");
            list.add(sysMapping.getSysMappingTableWidth());
        }
        if (sysMapping.getSysMappingSelect() != null && !sysMapping.getSysMappingSelect().toString().isEmpty()) {
            sb.append("sys_mapping_select,");
            list.add(sysMapping.getSysMappingSelect());
        }
        if (sysMapping.getSysMappingImage() != null && !sysMapping.getSysMappingImage().toString().isEmpty()) {
            sb.append("sys_mapping_image,");
            list.add(sysMapping.getSysMappingImage());
        }
        if (sysMapping.getSysMappingFile() != null && !sysMapping.getSysMappingFile().toString().isEmpty()) {
            sb.append("sys_mapping_file,");
            list.add(sysMapping.getSysMappingFile());
        }
        if (sysMapping.getSysMappingDate() != null && !sysMapping.getSysMappingDate().toString().isEmpty()) {
            sb.append("sys_mapping_date,");
            list.add(sysMapping.getSysMappingDate());
        }
        if (sysMapping.getSysMappingScript() != null && !sysMapping.getSysMappingScript().toString().isEmpty()) {
            sb.append("sys_mapping_script,");
            list.add(sysMapping.getSysMappingScript());
        }
        if (sysMapping.getSysMappingSort() != null && !sysMapping.getSysMappingSort().toString().isEmpty()) {
            sb.append("sys_mapping_sort,");
            list.add(sysMapping.getSysMappingSort());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysMapping sysMapping) {
        if (sysMapping.getSysMappingId() == null || sysMapping.getSysMappingId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_mapping set");
        List<Object> list = new ArrayList<>();
        if (sysMapping.getSysMappingKey() != null && !sysMapping.getSysMappingKey().toString().isEmpty()) {
            sb.append(" sys_mapping_key = ?,");
            list.add(sysMapping.getSysMappingKey());
        }
        if (sysMapping.getSysMappingTitle() != null && !sysMapping.getSysMappingTitle().toString().isEmpty()) {
            sb.append(" sys_mapping_title = ?,");
            list.add(sysMapping.getSysMappingTitle());
        }
        if (sysMapping.getSysMappingVal() != null && !sysMapping.getSysMappingVal().toString().isEmpty()) {
            sb.append(" sys_mapping_val = ?,");
            list.add(sysMapping.getSysMappingVal());
        }
        if (sysMapping.getSysMappingTableWidth() != null && !sysMapping.getSysMappingTableWidth().toString().isEmpty()) {
            sb.append(" sys_mapping_table_width = ?,");
            list.add(sysMapping.getSysMappingTableWidth());
        }
        if (sysMapping.getSysMappingSelect() != null && !sysMapping.getSysMappingSelect().toString().isEmpty()) {
            sb.append(" sys_mapping_select = ?,");
            list.add(sysMapping.getSysMappingSelect());
        }
        if (sysMapping.getSysMappingImage() != null && !sysMapping.getSysMappingImage().toString().isEmpty()) {
            sb.append(" sys_mapping_image = ?,");
            list.add(sysMapping.getSysMappingImage());
        }
        if (sysMapping.getSysMappingFile() != null && !sysMapping.getSysMappingFile().toString().isEmpty()) {
            sb.append(" sys_mapping_file = ?,");
            list.add(sysMapping.getSysMappingFile());
        }
        if (sysMapping.getSysMappingDate() != null && !sysMapping.getSysMappingDate().toString().isEmpty()) {
            sb.append(" sys_mapping_date = ?,");
            list.add(sysMapping.getSysMappingDate());
        }
        if (sysMapping.getSysMappingScript() != null && !sysMapping.getSysMappingScript().toString().isEmpty()) {
            sb.append(" sys_mapping_script = ?,");
            list.add(sysMapping.getSysMappingScript());
        }
        if (sysMapping.getSysMappingSort() != null && !sysMapping.getSysMappingSort().toString().isEmpty()) {
            sb.append(" sys_mapping_sort = ?,");
            list.add(sysMapping.getSysMappingSort());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_mapping_id = ?");
        list.add(sysMapping.getSysMappingId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysMapping sysMapping,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_mapping");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysMapping,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysMapping sysMapping,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_mapping_id", sysMapping.getSysMappingId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_mapping_id", sysMapping.getSysMappingId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_key", sysMapping.getSysMappingKey(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_title", sysMapping.getSysMappingTitle(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_val", sysMapping.getSysMappingVal(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_table_width", sysMapping.getSysMappingTableWidth(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_select", sysMapping.getSysMappingSelect(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_image", sysMapping.getSysMappingImage(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_file", sysMapping.getSysMappingFile(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_date", sysMapping.getSysMappingDate(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_script", sysMapping.getSysMappingScript(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_mapping_sort", sysMapping.getSysMappingSort(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysMapping asSysMapping(Map<String,String[]> requestData){
        List<SysMapping> list=asListSysMapping(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysMapping> asListSysMapping(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysMappingIdArray = requestData.get("sysMappingId");
        if (sysMappingIdArray!=null && sysMappingIdArray.length>0) {
            if (size<sysMappingIdArray.length) {
                size=sysMappingIdArray.length;
            }
        }
        String[] sysMappingKeyArray = requestData.get("sysMappingKey");
        if (sysMappingKeyArray!=null && sysMappingKeyArray.length>0) {
            if (size<sysMappingKeyArray.length) {
                size=sysMappingKeyArray.length;
            }
        }
        String[] sysMappingTitleArray = requestData.get("sysMappingTitle");
        if (sysMappingTitleArray!=null && sysMappingTitleArray.length>0) {
            if (size<sysMappingTitleArray.length) {
                size=sysMappingTitleArray.length;
            }
        }
        String[] sysMappingValArray = requestData.get("sysMappingVal");
        if (sysMappingValArray!=null && sysMappingValArray.length>0) {
            if (size<sysMappingValArray.length) {
                size=sysMappingValArray.length;
            }
        }
        String[] sysMappingTableWidthArray = requestData.get("sysMappingTableWidth");
        if (sysMappingTableWidthArray!=null && sysMappingTableWidthArray.length>0) {
            if (size<sysMappingTableWidthArray.length) {
                size=sysMappingTableWidthArray.length;
            }
        }
        String[] sysMappingSelectArray = requestData.get("sysMappingSelect");
        if (sysMappingSelectArray!=null && sysMappingSelectArray.length>0) {
            if (size<sysMappingSelectArray.length) {
                size=sysMappingSelectArray.length;
            }
        }
        String[] sysMappingImageArray = requestData.get("sysMappingImage");
        if (sysMappingImageArray!=null && sysMappingImageArray.length>0) {
            if (size<sysMappingImageArray.length) {
                size=sysMappingImageArray.length;
            }
        }
        String[] sysMappingFileArray = requestData.get("sysMappingFile");
        if (sysMappingFileArray!=null && sysMappingFileArray.length>0) {
            if (size<sysMappingFileArray.length) {
                size=sysMappingFileArray.length;
            }
        }
        String[] sysMappingDateArray = requestData.get("sysMappingDate");
        if (sysMappingDateArray!=null && sysMappingDateArray.length>0) {
            if (size<sysMappingDateArray.length) {
                size=sysMappingDateArray.length;
            }
        }
        String[] sysMappingScriptArray = requestData.get("sysMappingScript");
        if (sysMappingScriptArray!=null && sysMappingScriptArray.length>0) {
            if (size<sysMappingScriptArray.length) {
                size=sysMappingScriptArray.length;
            }
        }
        String[] sysMappingSortArray = requestData.get("sysMappingSort");
        if (sysMappingSortArray!=null && sysMappingSortArray.length>0) {
            if (size<sysMappingSortArray.length) {
                size=sysMappingSortArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysMapping> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysMapping sysMapping=new SysMapping();
            if (sysMappingIdArray!=null && i <= sysMappingIdArray.length-1) {
                sysMapping.setSysMappingId(java.lang.Long.valueOf(sysMappingIdArray[i]));
            }
            if (sysMappingKeyArray!=null && i <= sysMappingKeyArray.length-1) {
                sysMapping.setSysMappingKey(java.lang.String.valueOf(sysMappingKeyArray[i]));
            }
            if (sysMappingTitleArray!=null && i <= sysMappingTitleArray.length-1) {
                sysMapping.setSysMappingTitle(java.lang.String.valueOf(sysMappingTitleArray[i]));
            }
            if (sysMappingValArray!=null && i <= sysMappingValArray.length-1) {
                sysMapping.setSysMappingVal(java.lang.String.valueOf(sysMappingValArray[i]));
            }
            if (sysMappingTableWidthArray!=null && i <= sysMappingTableWidthArray.length-1) {
                sysMapping.setSysMappingTableWidth(java.lang.Long.valueOf(sysMappingTableWidthArray[i]));
            }
            if (sysMappingSelectArray!=null && i <= sysMappingSelectArray.length-1) {
                sysMapping.setSysMappingSelect(java.lang.String.valueOf(sysMappingSelectArray[i]));
            }
            if (sysMappingImageArray!=null && i <= sysMappingImageArray.length-1) {
                sysMapping.setSysMappingImage(java.lang.String.valueOf(sysMappingImageArray[i]));
            }
            if (sysMappingFileArray!=null && i <= sysMappingFileArray.length-1) {
                sysMapping.setSysMappingFile(java.lang.String.valueOf(sysMappingFileArray[i]));
            }
            if (sysMappingDateArray!=null && i <= sysMappingDateArray.length-1) {
                sysMapping.setSysMappingDate(java.lang.String.valueOf(sysMappingDateArray[i]));
            }
            if (sysMappingScriptArray!=null && i <= sysMappingScriptArray.length-1) {
                sysMapping.setSysMappingScript(java.lang.String.valueOf(sysMappingScriptArray[i]));
            }
            if (sysMappingSortArray!=null && i <= sysMappingSortArray.length-1) {
                sysMapping.setSysMappingSort(java.lang.Long.valueOf(sysMappingSortArray[i]));
            }
            list.add(sysMapping);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysOption sysOption) {
        return toSelectSql(sysOption,"sys_option_id", "asc", 0, false);
    }
    /**
     * 实体类[SysOption]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysOption sysOption,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_option");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysOption,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysOption sysOption,java.lang.Long sysOptionId) {
        if (sysOptionId > 0L) {
            sysOption.setSysOptionId(sysOptionId);
        } else {
            sysOption.setSysOptionId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_option(");
        List<Object> list = new ArrayList<>();
        if (sysOption.getSysOptionId() != null && !sysOption.getSysOptionId().toString().isEmpty()) {
            sb.append("sys_option_id,");
            list.add(sysOption.getSysOptionId());
        }
        if (sysOption.getSysOptionKey() != null && !sysOption.getSysOptionKey().toString().isEmpty()) {
            sb.append("sys_option_key,");
            list.add(sysOption.getSysOptionKey());
        }
        if (sysOption.getSysOptionTitle() != null && !sysOption.getSysOptionTitle().toString().isEmpty()) {
            sb.append("sys_option_title,");
            list.add(sysOption.getSysOptionTitle());
        }
        if (sysOption.getSysOptionValue() != null && !sysOption.getSysOptionValue().toString().isEmpty()) {
            sb.append("sys_option_value,");
            list.add(sysOption.getSysOptionValue());
        }
        if (sysOption.getSysOptionState() != null && !sysOption.getSysOptionState().toString().isEmpty()) {
            sb.append("sys_option_state,");
            list.add(sysOption.getSysOptionState());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysOption sysOption) {
        if (sysOption.getSysOptionId() == null || sysOption.getSysOptionId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_option set");
        List<Object> list = new ArrayList<>();
        if (sysOption.getSysOptionKey() != null && !sysOption.getSysOptionKey().toString().isEmpty()) {
            sb.append(" sys_option_key = ?,");
            list.add(sysOption.getSysOptionKey());
        }
        if (sysOption.getSysOptionTitle() != null && !sysOption.getSysOptionTitle().toString().isEmpty()) {
            sb.append(" sys_option_title = ?,");
            list.add(sysOption.getSysOptionTitle());
        }
        if (sysOption.getSysOptionValue() != null && !sysOption.getSysOptionValue().toString().isEmpty()) {
            sb.append(" sys_option_value = ?,");
            list.add(sysOption.getSysOptionValue());
        }
        if (sysOption.getSysOptionState() != null && !sysOption.getSysOptionState().toString().isEmpty()) {
            sb.append(" sys_option_state = ?,");
            list.add(sysOption.getSysOptionState());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_option_id = ?");
        list.add(sysOption.getSysOptionId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysOption sysOption,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_option");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysOption,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysOption sysOption,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_option_id", sysOption.getSysOptionId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_option_id", sysOption.getSysOptionId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_option_key", sysOption.getSysOptionKey(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_option_title", sysOption.getSysOptionTitle(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_option_value", sysOption.getSysOptionValue(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_option_state", sysOption.getSysOptionState(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysOption asSysOption(Map<String,String[]> requestData){
        List<SysOption> list=asListSysOption(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysOption> asListSysOption(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysOptionIdArray = requestData.get("sysOptionId");
        if (sysOptionIdArray!=null && sysOptionIdArray.length>0) {
            if (size<sysOptionIdArray.length) {
                size=sysOptionIdArray.length;
            }
        }
        String[] sysOptionKeyArray = requestData.get("sysOptionKey");
        if (sysOptionKeyArray!=null && sysOptionKeyArray.length>0) {
            if (size<sysOptionKeyArray.length) {
                size=sysOptionKeyArray.length;
            }
        }
        String[] sysOptionTitleArray = requestData.get("sysOptionTitle");
        if (sysOptionTitleArray!=null && sysOptionTitleArray.length>0) {
            if (size<sysOptionTitleArray.length) {
                size=sysOptionTitleArray.length;
            }
        }
        String[] sysOptionValueArray = requestData.get("sysOptionValue");
        if (sysOptionValueArray!=null && sysOptionValueArray.length>0) {
            if (size<sysOptionValueArray.length) {
                size=sysOptionValueArray.length;
            }
        }
        String[] sysOptionStateArray = requestData.get("sysOptionState");
        if (sysOptionStateArray!=null && sysOptionStateArray.length>0) {
            if (size<sysOptionStateArray.length) {
                size=sysOptionStateArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysOption> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysOption sysOption=new SysOption();
            if (sysOptionIdArray!=null && i <= sysOptionIdArray.length-1) {
                sysOption.setSysOptionId(java.lang.Long.valueOf(sysOptionIdArray[i]));
            }
            if (sysOptionKeyArray!=null && i <= sysOptionKeyArray.length-1) {
                sysOption.setSysOptionKey(java.lang.String.valueOf(sysOptionKeyArray[i]));
            }
            if (sysOptionTitleArray!=null && i <= sysOptionTitleArray.length-1) {
                sysOption.setSysOptionTitle(java.lang.String.valueOf(sysOptionTitleArray[i]));
            }
            if (sysOptionValueArray!=null && i <= sysOptionValueArray.length-1) {
                sysOption.setSysOptionValue(java.lang.String.valueOf(sysOptionValueArray[i]));
            }
            if (sysOptionStateArray!=null && i <= sysOptionStateArray.length-1) {
                sysOption.setSysOptionState(java.lang.Long.valueOf(sysOptionStateArray[i]));
            }
            list.add(sysOption);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysPermission sysPermission) {
        return toSelectSql(sysPermission,"sys_permission_id", "asc", 0, false);
    }
    /**
     * 实体类[SysPermission]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysPermission sysPermission,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_permission");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysPermission,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysPermission sysPermission,java.lang.Long sysPermissionId) {
        if (sysPermissionId > 0L) {
            sysPermission.setSysPermissionId(sysPermissionId);
        } else {
            sysPermission.setSysPermissionId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_permission(");
        List<Object> list = new ArrayList<>();
        if (sysPermission.getSysPermissionId() != null && !sysPermission.getSysPermissionId().toString().isEmpty()) {
            sb.append("sys_permission_id,");
            list.add(sysPermission.getSysPermissionId());
        }
        if (sysPermission.getSysPermissionName() != null && !sysPermission.getSysPermissionName().toString().isEmpty()) {
            sb.append("sys_permission_name,");
            list.add(sysPermission.getSysPermissionName());
        }
        if (sysPermission.getSysPermissionData() != null && !sysPermission.getSysPermissionData().toString().isEmpty()) {
            sb.append("sys_permission_data,");
            list.add(sysPermission.getSysPermissionData());
        }
        if (sysPermission.getSysPermissionType() != null && !sysPermission.getSysPermissionType().toString().isEmpty()) {
            sb.append("sys_permission_type,");
            list.add(sysPermission.getSysPermissionType());
        }
        if (sysPermission.getSysPermissionDesc() != null && !sysPermission.getSysPermissionDesc().toString().isEmpty()) {
            sb.append("sys_permission_desc,");
            list.add(sysPermission.getSysPermissionDesc());
        }
        if (sysPermission.getSysPermissionSup() != null && !sysPermission.getSysPermissionSup().toString().isEmpty()) {
            sb.append("sys_permission_sup,");
            list.add(sysPermission.getSysPermissionSup());
        }
        if (sysPermission.getSysPermissionSort() != null && !sysPermission.getSysPermissionSort().toString().isEmpty()) {
            sb.append("sys_permission_sort,");
            list.add(sysPermission.getSysPermissionSort());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysPermission sysPermission) {
        if (sysPermission.getSysPermissionId() == null || sysPermission.getSysPermissionId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_permission set");
        List<Object> list = new ArrayList<>();
        if (sysPermission.getSysPermissionName() != null && !sysPermission.getSysPermissionName().toString().isEmpty()) {
            sb.append(" sys_permission_name = ?,");
            list.add(sysPermission.getSysPermissionName());
        }
        if (sysPermission.getSysPermissionData() != null && !sysPermission.getSysPermissionData().toString().isEmpty()) {
            sb.append(" sys_permission_data = ?,");
            list.add(sysPermission.getSysPermissionData());
        }
        if (sysPermission.getSysPermissionType() != null && !sysPermission.getSysPermissionType().toString().isEmpty()) {
            sb.append(" sys_permission_type = ?,");
            list.add(sysPermission.getSysPermissionType());
        }
        if (sysPermission.getSysPermissionDesc() != null && !sysPermission.getSysPermissionDesc().toString().isEmpty()) {
            sb.append(" sys_permission_desc = ?,");
            list.add(sysPermission.getSysPermissionDesc());
        }
        if (sysPermission.getSysPermissionSup() != null && !sysPermission.getSysPermissionSup().toString().isEmpty()) {
            sb.append(" sys_permission_sup = ?,");
            list.add(sysPermission.getSysPermissionSup());
        }
        if (sysPermission.getSysPermissionSort() != null && !sysPermission.getSysPermissionSort().toString().isEmpty()) {
            sb.append(" sys_permission_sort = ?,");
            list.add(sysPermission.getSysPermissionSort());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_permission_id = ?");
        list.add(sysPermission.getSysPermissionId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysPermission sysPermission,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_permission");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysPermission,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysPermission sysPermission,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_permission_id", sysPermission.getSysPermissionId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_permission_id", sysPermission.getSysPermissionId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_permission_name", sysPermission.getSysPermissionName(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_permission_data", sysPermission.getSysPermissionData(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_permission_type", sysPermission.getSysPermissionType(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_permission_desc", sysPermission.getSysPermissionDesc(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_permission_sup", sysPermission.getSysPermissionSup(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_permission_sort", sysPermission.getSysPermissionSort(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysPermission asSysPermission(Map<String,String[]> requestData){
        List<SysPermission> list=asListSysPermission(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysPermission> asListSysPermission(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysPermissionIdArray = requestData.get("sysPermissionId");
        if (sysPermissionIdArray!=null && sysPermissionIdArray.length>0) {
            if (size<sysPermissionIdArray.length) {
                size=sysPermissionIdArray.length;
            }
        }
        String[] sysPermissionNameArray = requestData.get("sysPermissionName");
        if (sysPermissionNameArray!=null && sysPermissionNameArray.length>0) {
            if (size<sysPermissionNameArray.length) {
                size=sysPermissionNameArray.length;
            }
        }
        String[] sysPermissionDataArray = requestData.get("sysPermissionData");
        if (sysPermissionDataArray!=null && sysPermissionDataArray.length>0) {
            if (size<sysPermissionDataArray.length) {
                size=sysPermissionDataArray.length;
            }
        }
        String[] sysPermissionTypeArray = requestData.get("sysPermissionType");
        if (sysPermissionTypeArray!=null && sysPermissionTypeArray.length>0) {
            if (size<sysPermissionTypeArray.length) {
                size=sysPermissionTypeArray.length;
            }
        }
        String[] sysPermissionDescArray = requestData.get("sysPermissionDesc");
        if (sysPermissionDescArray!=null && sysPermissionDescArray.length>0) {
            if (size<sysPermissionDescArray.length) {
                size=sysPermissionDescArray.length;
            }
        }
        String[] sysPermissionSupArray = requestData.get("sysPermissionSup");
        if (sysPermissionSupArray!=null && sysPermissionSupArray.length>0) {
            if (size<sysPermissionSupArray.length) {
                size=sysPermissionSupArray.length;
            }
        }
        String[] sysPermissionSortArray = requestData.get("sysPermissionSort");
        if (sysPermissionSortArray!=null && sysPermissionSortArray.length>0) {
            if (size<sysPermissionSortArray.length) {
                size=sysPermissionSortArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysPermission> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysPermission sysPermission=new SysPermission();
            if (sysPermissionIdArray!=null && i <= sysPermissionIdArray.length-1) {
                sysPermission.setSysPermissionId(java.lang.Long.valueOf(sysPermissionIdArray[i]));
            }
            if (sysPermissionNameArray!=null && i <= sysPermissionNameArray.length-1) {
                sysPermission.setSysPermissionName(java.lang.String.valueOf(sysPermissionNameArray[i]));
            }
            if (sysPermissionDataArray!=null && i <= sysPermissionDataArray.length-1) {
                sysPermission.setSysPermissionData(java.lang.String.valueOf(sysPermissionDataArray[i]));
            }
            if (sysPermissionTypeArray!=null && i <= sysPermissionTypeArray.length-1) {
                sysPermission.setSysPermissionType(java.lang.Long.valueOf(sysPermissionTypeArray[i]));
            }
            if (sysPermissionDescArray!=null && i <= sysPermissionDescArray.length-1) {
                sysPermission.setSysPermissionDesc(java.lang.String.valueOf(sysPermissionDescArray[i]));
            }
            if (sysPermissionSupArray!=null && i <= sysPermissionSupArray.length-1) {
                sysPermission.setSysPermissionSup(java.lang.Long.valueOf(sysPermissionSupArray[i]));
            }
            if (sysPermissionSortArray!=null && i <= sysPermissionSortArray.length-1) {
                sysPermission.setSysPermissionSort(java.lang.Long.valueOf(sysPermissionSortArray[i]));
            }
            list.add(sysPermission);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysUsers sysUsers) {
        return toSelectSql(sysUsers,"sys_users_id", "asc", 0, false);
    }
    /**
     * 实体类[SysUsers]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysUsers sysUsers,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_users");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysUsers,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysUsers sysUsers,java.lang.Long sysUsersId) {
        if (sysUsersId > 0L) {
            sysUsers.setSysUsersId(sysUsersId);
        } else {
            sysUsers.setSysUsersId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_users(");
        List<Object> list = new ArrayList<>();
        if (sysUsers.getSysUsersId() != null && !sysUsers.getSysUsersId().toString().isEmpty()) {
            sb.append("sys_users_id,");
            list.add(sysUsers.getSysUsersId());
        }
        if (sysUsers.getSysUsersAcc() != null && !sysUsers.getSysUsersAcc().toString().isEmpty()) {
            sb.append("sys_users_acc,");
            list.add(sysUsers.getSysUsersAcc());
        }
        if (sysUsers.getSysUsersPwd() != null && !sysUsers.getSysUsersPwd().toString().isEmpty()) {
            sb.append("sys_users_pwd,");
            list.add(sysUsers.getSysUsersPwd());
        }
        if (sysUsers.getSysUsersPhone() != null && !sysUsers.getSysUsersPhone().toString().isEmpty()) {
            sb.append("sys_users_phone,");
            list.add(sysUsers.getSysUsersPhone());
        }
        if (sysUsers.getSysUsersOpenId() != null && !sysUsers.getSysUsersOpenId().toString().isEmpty()) {
            sb.append("sys_users_open_id,");
            list.add(sysUsers.getSysUsersOpenId());
        }
        if (sysUsers.getSysUsersStatus() != null && !sysUsers.getSysUsersStatus().toString().isEmpty()) {
            sb.append("sys_users_status,");
            list.add(sysUsers.getSysUsersStatus());
        }
        if (sysUsers.getSysUsersType() != null && !sysUsers.getSysUsersType().toString().isEmpty()) {
            sb.append("sys_users_type,");
            list.add(sysUsers.getSysUsersType());
        }
        if (sysUsers.getSysUsersRegTime() != null && !sysUsers.getSysUsersRegTime().toString().isEmpty()) {
            sb.append("sys_users_reg_time,");
            list.add(sysUsers.getSysUsersRegTime());
        }
        if (sysUsers.getSysUsersStartTime() != null && !sysUsers.getSysUsersStartTime().toString().isEmpty()) {
            sb.append("sys_users_start_time,");
            list.add(sysUsers.getSysUsersStartTime());
        }
        if (sysUsers.getSysUsersEndTime() != null && !sysUsers.getSysUsersEndTime().toString().isEmpty()) {
            sb.append("sys_users_end_time,");
            list.add(sysUsers.getSysUsersEndTime());
        }
        if (sysUsers.getSysUsersPrice() != null && !sysUsers.getSysUsersPrice().toString().isEmpty()) {
            sb.append("sys_users_price,");
            list.add(sysUsers.getSysUsersPrice());
        }
        if (sysUsers.getSysUsersDesc() != null && !sysUsers.getSysUsersDesc().toString().isEmpty()) {
            sb.append("sys_users_desc,");
            list.add(sysUsers.getSysUsersDesc());
        }
        if (sysUsers.getSysUsersSup() != null && !sysUsers.getSysUsersSup().toString().isEmpty()) {
            sb.append("sys_users_sup,");
            list.add(sysUsers.getSysUsersSup());
        }
        if (sysUsers.getSysUsersMail() != null && !sysUsers.getSysUsersMail().toString().isEmpty()) {
            sb.append("sys_users_mail,");
            list.add(sysUsers.getSysUsersMail());
        }
        if (sysUsers.getSysUsersGroup() != null && !sysUsers.getSysUsersGroup().toString().isEmpty()) {
            sb.append("sys_users_group,");
            list.add(sysUsers.getSysUsersGroup());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysUsers sysUsers) {
        if (sysUsers.getSysUsersId() == null || sysUsers.getSysUsersId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_users set");
        List<Object> list = new ArrayList<>();
        if (sysUsers.getSysUsersAcc() != null && !sysUsers.getSysUsersAcc().toString().isEmpty()) {
            sb.append(" sys_users_acc = ?,");
            list.add(sysUsers.getSysUsersAcc());
        }
        if (sysUsers.getSysUsersPwd() != null && !sysUsers.getSysUsersPwd().toString().isEmpty()) {
            sb.append(" sys_users_pwd = ?,");
            list.add(sysUsers.getSysUsersPwd());
        }
        if (sysUsers.getSysUsersPhone() != null && !sysUsers.getSysUsersPhone().toString().isEmpty()) {
            sb.append(" sys_users_phone = ?,");
            list.add(sysUsers.getSysUsersPhone());
        }
        if (sysUsers.getSysUsersOpenId() != null && !sysUsers.getSysUsersOpenId().toString().isEmpty()) {
            sb.append(" sys_users_open_id = ?,");
            list.add(sysUsers.getSysUsersOpenId());
        }
        if (sysUsers.getSysUsersStatus() != null && !sysUsers.getSysUsersStatus().toString().isEmpty()) {
            sb.append(" sys_users_status = ?,");
            list.add(sysUsers.getSysUsersStatus());
        }
        if (sysUsers.getSysUsersType() != null && !sysUsers.getSysUsersType().toString().isEmpty()) {
            sb.append(" sys_users_type = ?,");
            list.add(sysUsers.getSysUsersType());
        }
        if (sysUsers.getSysUsersRegTime() != null && !sysUsers.getSysUsersRegTime().toString().isEmpty()) {
            sb.append(" sys_users_reg_time = ?,");
            list.add(sysUsers.getSysUsersRegTime());
        }
        if (sysUsers.getSysUsersStartTime() != null && !sysUsers.getSysUsersStartTime().toString().isEmpty()) {
            sb.append(" sys_users_start_time = ?,");
            list.add(sysUsers.getSysUsersStartTime());
        }
        if (sysUsers.getSysUsersEndTime() != null && !sysUsers.getSysUsersEndTime().toString().isEmpty()) {
            sb.append(" sys_users_end_time = ?,");
            list.add(sysUsers.getSysUsersEndTime());
        }
        if (sysUsers.getSysUsersPrice() != null && !sysUsers.getSysUsersPrice().toString().isEmpty()) {
            sb.append(" sys_users_price = ?,");
            list.add(sysUsers.getSysUsersPrice());
        }
        if (sysUsers.getSysUsersDesc() != null && !sysUsers.getSysUsersDesc().toString().isEmpty()) {
            sb.append(" sys_users_desc = ?,");
            list.add(sysUsers.getSysUsersDesc());
        }
        if (sysUsers.getSysUsersSup() != null && !sysUsers.getSysUsersSup().toString().isEmpty()) {
            sb.append(" sys_users_sup = ?,");
            list.add(sysUsers.getSysUsersSup());
        }
        if (sysUsers.getSysUsersMail() != null && !sysUsers.getSysUsersMail().toString().isEmpty()) {
            sb.append(" sys_users_mail = ?,");
            list.add(sysUsers.getSysUsersMail());
        }
        if (sysUsers.getSysUsersGroup() != null && !sysUsers.getSysUsersGroup().toString().isEmpty()) {
            sb.append(" sys_users_group = ?,");
            list.add(sysUsers.getSysUsersGroup());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_users_id = ?");
        list.add(sysUsers.getSysUsersId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysUsers sysUsers,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_users");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysUsers,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysUsers sysUsers,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_users_id", sysUsers.getSysUsersId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_users_id", sysUsers.getSysUsersId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_acc", sysUsers.getSysUsersAcc(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_pwd", sysUsers.getSysUsersPwd(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_phone", sysUsers.getSysUsersPhone(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_open_id", sysUsers.getSysUsersOpenId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_status", sysUsers.getSysUsersStatus(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_type", sysUsers.getSysUsersType(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_reg_time", sysUsers.getSysUsersRegTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_start_time", sysUsers.getSysUsersStartTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_end_time", sysUsers.getSysUsersEndTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_price", sysUsers.getSysUsersPrice(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_desc", sysUsers.getSysUsersDesc(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_sup", sysUsers.getSysUsersSup(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_mail", sysUsers.getSysUsersMail(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_group", sysUsers.getSysUsersGroup(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysUsers asSysUsers(Map<String,String[]> requestData){
        List<SysUsers> list=asListSysUsers(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysUsers> asListSysUsers(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysUsersIdArray = requestData.get("sysUsersId");
        if (sysUsersIdArray!=null && sysUsersIdArray.length>0) {
            if (size<sysUsersIdArray.length) {
                size=sysUsersIdArray.length;
            }
        }
        String[] sysUsersAccArray = requestData.get("sysUsersAcc");
        if (sysUsersAccArray!=null && sysUsersAccArray.length>0) {
            if (size<sysUsersAccArray.length) {
                size=sysUsersAccArray.length;
            }
        }
        String[] sysUsersPwdArray = requestData.get("sysUsersPwd");
        if (sysUsersPwdArray!=null && sysUsersPwdArray.length>0) {
            if (size<sysUsersPwdArray.length) {
                size=sysUsersPwdArray.length;
            }
        }
        String[] sysUsersPhoneArray = requestData.get("sysUsersPhone");
        if (sysUsersPhoneArray!=null && sysUsersPhoneArray.length>0) {
            if (size<sysUsersPhoneArray.length) {
                size=sysUsersPhoneArray.length;
            }
        }
        String[] sysUsersOpenIdArray = requestData.get("sysUsersOpenId");
        if (sysUsersOpenIdArray!=null && sysUsersOpenIdArray.length>0) {
            if (size<sysUsersOpenIdArray.length) {
                size=sysUsersOpenIdArray.length;
            }
        }
        String[] sysUsersStatusArray = requestData.get("sysUsersStatus");
        if (sysUsersStatusArray!=null && sysUsersStatusArray.length>0) {
            if (size<sysUsersStatusArray.length) {
                size=sysUsersStatusArray.length;
            }
        }
        String[] sysUsersTypeArray = requestData.get("sysUsersType");
        if (sysUsersTypeArray!=null && sysUsersTypeArray.length>0) {
            if (size<sysUsersTypeArray.length) {
                size=sysUsersTypeArray.length;
            }
        }
        String[] sysUsersRegTimeArray = requestData.get("sysUsersRegTime");
        if (sysUsersRegTimeArray!=null && sysUsersRegTimeArray.length>0) {
            if (size<sysUsersRegTimeArray.length) {
                size=sysUsersRegTimeArray.length;
            }
        }
        String[] sysUsersStartTimeArray = requestData.get("sysUsersStartTime");
        if (sysUsersStartTimeArray!=null && sysUsersStartTimeArray.length>0) {
            if (size<sysUsersStartTimeArray.length) {
                size=sysUsersStartTimeArray.length;
            }
        }
        String[] sysUsersEndTimeArray = requestData.get("sysUsersEndTime");
        if (sysUsersEndTimeArray!=null && sysUsersEndTimeArray.length>0) {
            if (size<sysUsersEndTimeArray.length) {
                size=sysUsersEndTimeArray.length;
            }
        }
        String[] sysUsersPriceArray = requestData.get("sysUsersPrice");
        if (sysUsersPriceArray!=null && sysUsersPriceArray.length>0) {
            if (size<sysUsersPriceArray.length) {
                size=sysUsersPriceArray.length;
            }
        }
        String[] sysUsersDescArray = requestData.get("sysUsersDesc");
        if (sysUsersDescArray!=null && sysUsersDescArray.length>0) {
            if (size<sysUsersDescArray.length) {
                size=sysUsersDescArray.length;
            }
        }
        String[] sysUsersSupArray = requestData.get("sysUsersSup");
        if (sysUsersSupArray!=null && sysUsersSupArray.length>0) {
            if (size<sysUsersSupArray.length) {
                size=sysUsersSupArray.length;
            }
        }
        String[] sysUsersMailArray = requestData.get("sysUsersMail");
        if (sysUsersMailArray!=null && sysUsersMailArray.length>0) {
            if (size<sysUsersMailArray.length) {
                size=sysUsersMailArray.length;
            }
        }
        String[] sysUsersGroupArray = requestData.get("sysUsersGroup");
        if (sysUsersGroupArray!=null && sysUsersGroupArray.length>0) {
            if (size<sysUsersGroupArray.length) {
                size=sysUsersGroupArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysUsers> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysUsers sysUsers=new SysUsers();
            if (sysUsersIdArray!=null && i <= sysUsersIdArray.length-1) {
                sysUsers.setSysUsersId(java.lang.Long.valueOf(sysUsersIdArray[i]));
            }
            if (sysUsersAccArray!=null && i <= sysUsersAccArray.length-1) {
                sysUsers.setSysUsersAcc(java.lang.String.valueOf(sysUsersAccArray[i]));
            }
            if (sysUsersPwdArray!=null && i <= sysUsersPwdArray.length-1) {
                sysUsers.setSysUsersPwd(java.lang.String.valueOf(sysUsersPwdArray[i]));
            }
            if (sysUsersPhoneArray!=null && i <= sysUsersPhoneArray.length-1) {
                sysUsers.setSysUsersPhone(java.lang.String.valueOf(sysUsersPhoneArray[i]));
            }
            if (sysUsersOpenIdArray!=null && i <= sysUsersOpenIdArray.length-1) {
                sysUsers.setSysUsersOpenId(java.lang.String.valueOf(sysUsersOpenIdArray[i]));
            }
            if (sysUsersStatusArray!=null && i <= sysUsersStatusArray.length-1) {
                sysUsers.setSysUsersStatus(java.lang.Long.valueOf(sysUsersStatusArray[i]));
            }
            if (sysUsersTypeArray!=null && i <= sysUsersTypeArray.length-1) {
                sysUsers.setSysUsersType(java.lang.Long.valueOf(sysUsersTypeArray[i]));
            }
            if (sysUsersRegTimeArray!=null && i <= sysUsersRegTimeArray.length-1) {
                sysUsers.setSysUsersRegTime(java.lang.String.valueOf(sysUsersRegTimeArray[i]));
            }
            if (sysUsersStartTimeArray!=null && i <= sysUsersStartTimeArray.length-1) {
                sysUsers.setSysUsersStartTime(java.lang.String.valueOf(sysUsersStartTimeArray[i]));
            }
            if (sysUsersEndTimeArray!=null && i <= sysUsersEndTimeArray.length-1) {
                sysUsers.setSysUsersEndTime(java.lang.String.valueOf(sysUsersEndTimeArray[i]));
            }
            if (sysUsersPriceArray!=null && i <= sysUsersPriceArray.length-1) {
                sysUsers.setSysUsersPrice(java.lang.Long.valueOf(sysUsersPriceArray[i]));
            }
            if (sysUsersDescArray!=null && i <= sysUsersDescArray.length-1) {
                sysUsers.setSysUsersDesc(java.lang.String.valueOf(sysUsersDescArray[i]));
            }
            if (sysUsersSupArray!=null && i <= sysUsersSupArray.length-1) {
                sysUsers.setSysUsersSup(java.lang.Long.valueOf(sysUsersSupArray[i]));
            }
            if (sysUsersMailArray!=null && i <= sysUsersMailArray.length-1) {
                sysUsers.setSysUsersMail(java.lang.String.valueOf(sysUsersMailArray[i]));
            }
            if (sysUsersGroupArray!=null && i <= sysUsersGroupArray.length-1) {
                sysUsers.setSysUsersGroup(java.lang.Long.valueOf(sysUsersGroupArray[i]));
            }
            list.add(sysUsers);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(SysUsersLoginLog sysUsersLoginLog) {
        return toSelectSql(sysUsersLoginLog,"sys_users_login_log_id", "asc", 0, false);
    }
    /**
     * 实体类[SysUsersLoginLog]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(SysUsersLoginLog sysUsersLoginLog,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from sys_users_login_log");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysUsersLoginLog,true,false, sb, list, selectId);
        if (sortField != null && sortType != null) {
            sb.append(" order by");
            sb.append(" ");
            sb.append(sortField);
            sb.append(" ");
            sb.append(sortType);
        }
        if (size > 0) {
            sb.append(" limit ?,?");
            list.add(0);
            list.add(size);
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //插入 可以指定id  不指定自动生成
    public static SqlTemplate toSave(SysUsersLoginLog sysUsersLoginLog,java.lang.Long sysUsersLoginLogId) {
        if (sysUsersLoginLogId > 0L) {
            sysUsersLoginLog.setSysUsersLoginLogId(sysUsersLoginLogId);
        } else {
            sysUsersLoginLog.setSysUsersLoginLogId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into sys_users_login_log(");
        List<Object> list = new ArrayList<>();
        if (sysUsersLoginLog.getSysUsersLoginLogId() != null && !sysUsersLoginLog.getSysUsersLoginLogId().toString().isEmpty()) {
            sb.append("sys_users_login_log_id,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogId());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogIp() != null && !sysUsersLoginLog.getSysUsersLoginLogIp().toString().isEmpty()) {
            sb.append("sys_users_login_log_ip,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogIp());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogDesc() != null && !sysUsersLoginLog.getSysUsersLoginLogDesc().toString().isEmpty()) {
            sb.append("sys_users_login_log_desc,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogDesc());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogTime() != null && !sysUsersLoginLog.getSysUsersLoginLogTime().toString().isEmpty()) {
            sb.append("sys_users_login_log_time,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogTime());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogUid() != null && !sysUsersLoginLog.getSysUsersLoginLogUid().toString().isEmpty()) {
            sb.append("sys_users_login_log_uid,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogUid());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogToken() != null && !sysUsersLoginLog.getSysUsersLoginLogToken().toString().isEmpty()) {
            sb.append("sys_users_login_log_token,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogToken());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogMac() != null && !sysUsersLoginLog.getSysUsersLoginLogMac().toString().isEmpty()) {
            sb.append("sys_users_login_log_mac,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogMac());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(") values (");
        for (int i = 0; i < list.size(); i++) {
            sb.append("?");
            if (i < (list.size() - 1)) {
                sb.append(",");
            }

        }
        sb.append(")");
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //根据id修改 高级需求请手动写sql
    public static SqlTemplate toUpdate(SysUsersLoginLog sysUsersLoginLog) {
        if (sysUsersLoginLog.getSysUsersLoginLogId() == null || sysUsersLoginLog.getSysUsersLoginLogId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update sys_users_login_log set");
        List<Object> list = new ArrayList<>();
        if (sysUsersLoginLog.getSysUsersLoginLogIp() != null && !sysUsersLoginLog.getSysUsersLoginLogIp().toString().isEmpty()) {
            sb.append(" sys_users_login_log_ip = ?,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogIp());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogDesc() != null && !sysUsersLoginLog.getSysUsersLoginLogDesc().toString().isEmpty()) {
            sb.append(" sys_users_login_log_desc = ?,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogDesc());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogTime() != null && !sysUsersLoginLog.getSysUsersLoginLogTime().toString().isEmpty()) {
            sb.append(" sys_users_login_log_time = ?,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogTime());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogUid() != null && !sysUsersLoginLog.getSysUsersLoginLogUid().toString().isEmpty()) {
            sb.append(" sys_users_login_log_uid = ?,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogUid());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogToken() != null && !sysUsersLoginLog.getSysUsersLoginLogToken().toString().isEmpty()) {
            sb.append(" sys_users_login_log_token = ?,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogToken());
        }
        if (sysUsersLoginLog.getSysUsersLoginLogMac() != null && !sysUsersLoginLog.getSysUsersLoginLogMac().toString().isEmpty()) {
            sb.append(" sys_users_login_log_mac = ?,");
            list.add(sysUsersLoginLog.getSysUsersLoginLogMac());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where sys_users_login_log_id = ?");
        list.add(sysUsersLoginLog.getSysUsersLoginLogId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(SysUsersLoginLog sysUsersLoginLog,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from sys_users_login_log");
        List<Object> list = new ArrayList<>();
        getSelectWhere(sysUsersLoginLog,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(SysUsersLoginLog sysUsersLoginLog,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("sys_users_login_log_id", sysUsersLoginLog.getSysUsersLoginLogId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("sys_users_login_log_id", sysUsersLoginLog.getSysUsersLoginLogId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_login_log_ip", sysUsersLoginLog.getSysUsersLoginLogIp(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_login_log_desc", sysUsersLoginLog.getSysUsersLoginLogDesc(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_login_log_time", sysUsersLoginLog.getSysUsersLoginLogTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_login_log_uid", sysUsersLoginLog.getSysUsersLoginLogUid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_login_log_token", sysUsersLoginLog.getSysUsersLoginLogToken(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("sys_users_login_log_mac", sysUsersLoginLog.getSysUsersLoginLogMac(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static SysUsersLoginLog asSysUsersLoginLog(Map<String,String[]> requestData){
        List<SysUsersLoginLog> list=asListSysUsersLoginLog(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<SysUsersLoginLog> asListSysUsersLoginLog(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] sysUsersLoginLogIdArray = requestData.get("sysUsersLoginLogId");
        if (sysUsersLoginLogIdArray!=null && sysUsersLoginLogIdArray.length>0) {
            if (size<sysUsersLoginLogIdArray.length) {
                size=sysUsersLoginLogIdArray.length;
            }
        }
        String[] sysUsersLoginLogIpArray = requestData.get("sysUsersLoginLogIp");
        if (sysUsersLoginLogIpArray!=null && sysUsersLoginLogIpArray.length>0) {
            if (size<sysUsersLoginLogIpArray.length) {
                size=sysUsersLoginLogIpArray.length;
            }
        }
        String[] sysUsersLoginLogDescArray = requestData.get("sysUsersLoginLogDesc");
        if (sysUsersLoginLogDescArray!=null && sysUsersLoginLogDescArray.length>0) {
            if (size<sysUsersLoginLogDescArray.length) {
                size=sysUsersLoginLogDescArray.length;
            }
        }
        String[] sysUsersLoginLogTimeArray = requestData.get("sysUsersLoginLogTime");
        if (sysUsersLoginLogTimeArray!=null && sysUsersLoginLogTimeArray.length>0) {
            if (size<sysUsersLoginLogTimeArray.length) {
                size=sysUsersLoginLogTimeArray.length;
            }
        }
        String[] sysUsersLoginLogUidArray = requestData.get("sysUsersLoginLogUid");
        if (sysUsersLoginLogUidArray!=null && sysUsersLoginLogUidArray.length>0) {
            if (size<sysUsersLoginLogUidArray.length) {
                size=sysUsersLoginLogUidArray.length;
            }
        }
        String[] sysUsersLoginLogTokenArray = requestData.get("sysUsersLoginLogToken");
        if (sysUsersLoginLogTokenArray!=null && sysUsersLoginLogTokenArray.length>0) {
            if (size<sysUsersLoginLogTokenArray.length) {
                size=sysUsersLoginLogTokenArray.length;
            }
        }
        String[] sysUsersLoginLogMacArray = requestData.get("sysUsersLoginLogMac");
        if (sysUsersLoginLogMacArray!=null && sysUsersLoginLogMacArray.length>0) {
            if (size<sysUsersLoginLogMacArray.length) {
                size=sysUsersLoginLogMacArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<SysUsersLoginLog> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SysUsersLoginLog sysUsersLoginLog=new SysUsersLoginLog();
            if (sysUsersLoginLogIdArray!=null && i <= sysUsersLoginLogIdArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogId(java.lang.Long.valueOf(sysUsersLoginLogIdArray[i]));
            }
            if (sysUsersLoginLogIpArray!=null && i <= sysUsersLoginLogIpArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogIp(java.lang.String.valueOf(sysUsersLoginLogIpArray[i]));
            }
            if (sysUsersLoginLogDescArray!=null && i <= sysUsersLoginLogDescArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogDesc(java.lang.String.valueOf(sysUsersLoginLogDescArray[i]));
            }
            if (sysUsersLoginLogTimeArray!=null && i <= sysUsersLoginLogTimeArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogTime(java.lang.String.valueOf(sysUsersLoginLogTimeArray[i]));
            }
            if (sysUsersLoginLogUidArray!=null && i <= sysUsersLoginLogUidArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogUid(java.lang.Long.valueOf(sysUsersLoginLogUidArray[i]));
            }
            if (sysUsersLoginLogTokenArray!=null && i <= sysUsersLoginLogTokenArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogToken(java.lang.String.valueOf(sysUsersLoginLogTokenArray[i]));
            }
            if (sysUsersLoginLogMacArray!=null && i <= sysUsersLoginLogMacArray.length-1) {
                sysUsersLoginLog.setSysUsersLoginLogMac(java.lang.String.valueOf(sysUsersLoginLogMacArray[i]));
            }
            list.add(sysUsersLoginLog);
        }
        return list;
    }

}
