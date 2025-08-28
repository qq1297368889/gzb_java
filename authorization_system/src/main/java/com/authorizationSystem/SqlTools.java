package com.authorizationSystem;

import gzb.entity.SqlTemplate;
import com.authorizationSystem.entity.*;
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
    public static SqlTemplate toSelectSql(Application application) {
        return toSelectSql(application,"application_id", "asc", 0, false);
    }
    /**
     * 实体类[Application]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(Application application,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from application");
        List<Object> list = new ArrayList<>();
        getSelectWhere(application,true,false, sb, list, selectId);
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
    public static SqlTemplate toSave(Application application,java.lang.Long applicationId) {
        if (applicationId > 0L) {
            application.setApplicationId(applicationId);
        } else {
            application.setApplicationId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into application(");
        List<Object> list = new ArrayList<>();
        if (application.getApplicationId() != null && !application.getApplicationId().toString().isEmpty()) {
            sb.append("application_id,");
            list.add(application.getApplicationId());
        }
        if (application.getApplicationName() != null && !application.getApplicationName().toString().isEmpty()) {
            sb.append("application_name,");
            list.add(application.getApplicationName());
        }
        if (application.getApplicationDesc() != null && !application.getApplicationDesc().toString().isEmpty()) {
            sb.append("application_desc,");
            list.add(application.getApplicationDesc());
        }
        if (application.getApplicationState() != null && !application.getApplicationState().toString().isEmpty()) {
            sb.append("application_state,");
            list.add(application.getApplicationState());
        }
        if (application.getApplicationType() != null && !application.getApplicationType().toString().isEmpty()) {
            sb.append("application_type,");
            list.add(application.getApplicationType());
        }
        if (application.getApplicationPwd() != null && !application.getApplicationPwd().toString().isEmpty()) {
            sb.append("application_pwd,");
            list.add(application.getApplicationPwd());
        }
        if (application.getApplicationIv() != null && !application.getApplicationIv().toString().isEmpty()) {
            sb.append("application_iv,");
            list.add(application.getApplicationIv());
        }
        if (application.getApplicationUid() != null && !application.getApplicationUid().toString().isEmpty()) {
            sb.append("application_uid,");
            list.add(application.getApplicationUid());
        }
        if (application.getApplicationCid() != null && !application.getApplicationCid().toString().isEmpty()) {
            sb.append("application_cid,");
            list.add(application.getApplicationCid());
        }
        if (application.getApplicationUiid() != null && !application.getApplicationUiid().toString().isEmpty()) {
            sb.append("application_uiid,");
            list.add(application.getApplicationUiid());
        }
        if (application.getApplicationSell() != null && !application.getApplicationSell().toString().isEmpty()) {
            sb.append("application_sell,");
            list.add(application.getApplicationSell());
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
    public static SqlTemplate toUpdate(Application application) {
        if (application.getApplicationId() == null || application.getApplicationId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update application set");
        List<Object> list = new ArrayList<>();
        if (application.getApplicationName() != null && !application.getApplicationName().toString().isEmpty()) {
            sb.append(" application_name = ?,");
            list.add(application.getApplicationName());
        }
        if (application.getApplicationDesc() != null && !application.getApplicationDesc().toString().isEmpty()) {
            sb.append(" application_desc = ?,");
            list.add(application.getApplicationDesc());
        }
        if (application.getApplicationState() != null && !application.getApplicationState().toString().isEmpty()) {
            sb.append(" application_state = ?,");
            list.add(application.getApplicationState());
        }
        if (application.getApplicationType() != null && !application.getApplicationType().toString().isEmpty()) {
            sb.append(" application_type = ?,");
            list.add(application.getApplicationType());
        }
        if (application.getApplicationPwd() != null && !application.getApplicationPwd().toString().isEmpty()) {
            sb.append(" application_pwd = ?,");
            list.add(application.getApplicationPwd());
        }
        if (application.getApplicationIv() != null && !application.getApplicationIv().toString().isEmpty()) {
            sb.append(" application_iv = ?,");
            list.add(application.getApplicationIv());
        }
        if (application.getApplicationUid() != null && !application.getApplicationUid().toString().isEmpty()) {
            sb.append(" application_uid = ?,");
            list.add(application.getApplicationUid());
        }
        if (application.getApplicationCid() != null && !application.getApplicationCid().toString().isEmpty()) {
            sb.append(" application_cid = ?,");
            list.add(application.getApplicationCid());
        }
        if (application.getApplicationUiid() != null && !application.getApplicationUiid().toString().isEmpty()) {
            sb.append(" application_uiid = ?,");
            list.add(application.getApplicationUiid());
        }
        if (application.getApplicationSell() != null && !application.getApplicationSell().toString().isEmpty()) {
            sb.append(" application_sell = ?,");
            list.add(application.getApplicationSell());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where application_id = ?");
        list.add(application.getApplicationId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(Application application,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from application");
        List<Object> list = new ArrayList<>();
        getSelectWhere(application,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(Application application,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("application_id", application.getApplicationId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("application_id", application.getApplicationId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_name", application.getApplicationName(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_desc", application.getApplicationDesc(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_state", application.getApplicationState(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_type", application.getApplicationType(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_pwd", application.getApplicationPwd(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_iv", application.getApplicationIv(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_uid", application.getApplicationUid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_cid", application.getApplicationCid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_uiid", application.getApplicationUiid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_sell", application.getApplicationSell(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static Application asApplication(Map<String,String[]> requestData){
        List<Application> list=asListApplication(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<Application> asListApplication(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] applicationIdArray = requestData.get("applicationId");
        if (applicationIdArray!=null && applicationIdArray.length>0) {
            if (size<applicationIdArray.length) {
                size=applicationIdArray.length;
            }
        }
        String[] applicationNameArray = requestData.get("applicationName");
        if (applicationNameArray!=null && applicationNameArray.length>0) {
            if (size<applicationNameArray.length) {
                size=applicationNameArray.length;
            }
        }
        String[] applicationDescArray = requestData.get("applicationDesc");
        if (applicationDescArray!=null && applicationDescArray.length>0) {
            if (size<applicationDescArray.length) {
                size=applicationDescArray.length;
            }
        }
        String[] applicationStateArray = requestData.get("applicationState");
        if (applicationStateArray!=null && applicationStateArray.length>0) {
            if (size<applicationStateArray.length) {
                size=applicationStateArray.length;
            }
        }
        String[] applicationTypeArray = requestData.get("applicationType");
        if (applicationTypeArray!=null && applicationTypeArray.length>0) {
            if (size<applicationTypeArray.length) {
                size=applicationTypeArray.length;
            }
        }
        String[] applicationPwdArray = requestData.get("applicationPwd");
        if (applicationPwdArray!=null && applicationPwdArray.length>0) {
            if (size<applicationPwdArray.length) {
                size=applicationPwdArray.length;
            }
        }
        String[] applicationIvArray = requestData.get("applicationIv");
        if (applicationIvArray!=null && applicationIvArray.length>0) {
            if (size<applicationIvArray.length) {
                size=applicationIvArray.length;
            }
        }
        String[] applicationUidArray = requestData.get("applicationUid");
        if (applicationUidArray!=null && applicationUidArray.length>0) {
            if (size<applicationUidArray.length) {
                size=applicationUidArray.length;
            }
        }
        String[] applicationCidArray = requestData.get("applicationCid");
        if (applicationCidArray!=null && applicationCidArray.length>0) {
            if (size<applicationCidArray.length) {
                size=applicationCidArray.length;
            }
        }
        String[] applicationUiidArray = requestData.get("applicationUiid");
        if (applicationUiidArray!=null && applicationUiidArray.length>0) {
            if (size<applicationUiidArray.length) {
                size=applicationUiidArray.length;
            }
        }
        String[] applicationSellArray = requestData.get("applicationSell");
        if (applicationSellArray!=null && applicationSellArray.length>0) {
            if (size<applicationSellArray.length) {
                size=applicationSellArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<Application> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Application application=new Application();
            if (applicationIdArray!=null && i <= applicationIdArray.length-1) {
                application.setApplicationId(java.lang.Long.valueOf(applicationIdArray[i]));
            }
            if (applicationNameArray!=null && i <= applicationNameArray.length-1) {
                application.setApplicationName(java.lang.String.valueOf(applicationNameArray[i]));
            }
            if (applicationDescArray!=null && i <= applicationDescArray.length-1) {
                application.setApplicationDesc(java.lang.String.valueOf(applicationDescArray[i]));
            }
            if (applicationStateArray!=null && i <= applicationStateArray.length-1) {
                application.setApplicationState(java.lang.Long.valueOf(applicationStateArray[i]));
            }
            if (applicationTypeArray!=null && i <= applicationTypeArray.length-1) {
                application.setApplicationType(java.lang.Long.valueOf(applicationTypeArray[i]));
            }
            if (applicationPwdArray!=null && i <= applicationPwdArray.length-1) {
                application.setApplicationPwd(java.lang.String.valueOf(applicationPwdArray[i]));
            }
            if (applicationIvArray!=null && i <= applicationIvArray.length-1) {
                application.setApplicationIv(java.lang.String.valueOf(applicationIvArray[i]));
            }
            if (applicationUidArray!=null && i <= applicationUidArray.length-1) {
                application.setApplicationUid(java.lang.Long.valueOf(applicationUidArray[i]));
            }
            if (applicationCidArray!=null && i <= applicationCidArray.length-1) {
                application.setApplicationCid(java.lang.Long.valueOf(applicationCidArray[i]));
            }
            if (applicationUiidArray!=null && i <= applicationUiidArray.length-1) {
                application.setApplicationUiid(java.lang.Long.valueOf(applicationUiidArray[i]));
            }
            if (applicationSellArray!=null && i <= applicationSellArray.length-1) {
                application.setApplicationSell(java.lang.Long.valueOf(applicationSellArray[i]));
            }
            list.add(application);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(ApplicationCode applicationCode) {
        return toSelectSql(applicationCode,"application_code_id", "asc", 0, false);
    }
    /**
     * 实体类[ApplicationCode]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(ApplicationCode applicationCode,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from application_code");
        List<Object> list = new ArrayList<>();
        getSelectWhere(applicationCode,true,false, sb, list, selectId);
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
    public static SqlTemplate toSave(ApplicationCode applicationCode,java.lang.Long applicationCodeId) {
        if (applicationCodeId > 0L) {
            applicationCode.setApplicationCodeId(applicationCodeId);
        } else {
            applicationCode.setApplicationCodeId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into application_code(");
        List<Object> list = new ArrayList<>();
        if (applicationCode.getApplicationCodeId() != null && !applicationCode.getApplicationCodeId().toString().isEmpty()) {
            sb.append("application_code_id,");
            list.add(applicationCode.getApplicationCodeId());
        }
        if (applicationCode.getApplicationCodeAid() != null && !applicationCode.getApplicationCodeAid().toString().isEmpty()) {
            sb.append("application_code_aid,");
            list.add(applicationCode.getApplicationCodeAid());
        }
        if (applicationCode.getApplicationCodeTime() != null && !applicationCode.getApplicationCodeTime().toString().isEmpty()) {
            sb.append("application_code_time,");
            list.add(applicationCode.getApplicationCodeTime());
        }
        if (applicationCode.getApplicationCodeFile() != null && !applicationCode.getApplicationCodeFile().toString().isEmpty()) {
            sb.append("application_code_file,");
            list.add(applicationCode.getApplicationCodeFile());
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
    public static SqlTemplate toUpdate(ApplicationCode applicationCode) {
        if (applicationCode.getApplicationCodeId() == null || applicationCode.getApplicationCodeId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update application_code set");
        List<Object> list = new ArrayList<>();
        if (applicationCode.getApplicationCodeAid() != null && !applicationCode.getApplicationCodeAid().toString().isEmpty()) {
            sb.append(" application_code_aid = ?,");
            list.add(applicationCode.getApplicationCodeAid());
        }
        if (applicationCode.getApplicationCodeTime() != null && !applicationCode.getApplicationCodeTime().toString().isEmpty()) {
            sb.append(" application_code_time = ?,");
            list.add(applicationCode.getApplicationCodeTime());
        }
        if (applicationCode.getApplicationCodeFile() != null && !applicationCode.getApplicationCodeFile().toString().isEmpty()) {
            sb.append(" application_code_file = ?,");
            list.add(applicationCode.getApplicationCodeFile());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where application_code_id = ?");
        list.add(applicationCode.getApplicationCodeId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(ApplicationCode applicationCode,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from application_code");
        List<Object> list = new ArrayList<>();
        getSelectWhere(applicationCode,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(ApplicationCode applicationCode,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("application_code_id", applicationCode.getApplicationCodeId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("application_code_id", applicationCode.getApplicationCodeId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_code_aid", applicationCode.getApplicationCodeAid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_code_time", applicationCode.getApplicationCodeTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_code_file", applicationCode.getApplicationCodeFile(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static ApplicationCode asApplicationCode(Map<String,String[]> requestData){
        List<ApplicationCode> list=asListApplicationCode(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<ApplicationCode> asListApplicationCode(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] applicationCodeIdArray = requestData.get("applicationCodeId");
        if (applicationCodeIdArray!=null && applicationCodeIdArray.length>0) {
            if (size<applicationCodeIdArray.length) {
                size=applicationCodeIdArray.length;
            }
        }
        String[] applicationCodeAidArray = requestData.get("applicationCodeAid");
        if (applicationCodeAidArray!=null && applicationCodeAidArray.length>0) {
            if (size<applicationCodeAidArray.length) {
                size=applicationCodeAidArray.length;
            }
        }
        String[] applicationCodeTimeArray = requestData.get("applicationCodeTime");
        if (applicationCodeTimeArray!=null && applicationCodeTimeArray.length>0) {
            if (size<applicationCodeTimeArray.length) {
                size=applicationCodeTimeArray.length;
            }
        }
        String[] applicationCodeFileArray = requestData.get("applicationCodeFile");
        if (applicationCodeFileArray!=null && applicationCodeFileArray.length>0) {
            if (size<applicationCodeFileArray.length) {
                size=applicationCodeFileArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<ApplicationCode> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ApplicationCode applicationCode=new ApplicationCode();
            if (applicationCodeIdArray!=null && i <= applicationCodeIdArray.length-1) {
                applicationCode.setApplicationCodeId(java.lang.Long.valueOf(applicationCodeIdArray[i]));
            }
            if (applicationCodeAidArray!=null && i <= applicationCodeAidArray.length-1) {
                applicationCode.setApplicationCodeAid(java.lang.Long.valueOf(applicationCodeAidArray[i]));
            }
            if (applicationCodeTimeArray!=null && i <= applicationCodeTimeArray.length-1) {
                applicationCode.setApplicationCodeTime(java.lang.String.valueOf(applicationCodeTimeArray[i]));
            }
            if (applicationCodeFileArray!=null && i <= applicationCodeFileArray.length-1) {
                applicationCode.setApplicationCodeFile(java.lang.Long.valueOf(applicationCodeFileArray[i]));
            }
            list.add(applicationCode);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(ApplicationRechargeCard applicationRechargeCard) {
        return toSelectSql(applicationRechargeCard,"application_recharge_card_id", "asc", 0, false);
    }
    /**
     * 实体类[ApplicationRechargeCard]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(ApplicationRechargeCard applicationRechargeCard,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from application_recharge_card");
        List<Object> list = new ArrayList<>();
        getSelectWhere(applicationRechargeCard,true,false, sb, list, selectId);
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
    public static SqlTemplate toSave(ApplicationRechargeCard applicationRechargeCard,java.lang.Long applicationRechargeCardId) {
        if (applicationRechargeCardId > 0L) {
            applicationRechargeCard.setApplicationRechargeCardId(applicationRechargeCardId);
        } else {
            applicationRechargeCard.setApplicationRechargeCardId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into application_recharge_card(");
        List<Object> list = new ArrayList<>();
        if (applicationRechargeCard.getApplicationRechargeCardId() != null && !applicationRechargeCard.getApplicationRechargeCardId().toString().isEmpty()) {
            sb.append("application_recharge_card_id,");
            list.add(applicationRechargeCard.getApplicationRechargeCardId());
        }
        if (applicationRechargeCard.getApplicationRechargeCardKey() != null && !applicationRechargeCard.getApplicationRechargeCardKey().toString().isEmpty()) {
            sb.append("application_recharge_card_key,");
            list.add(applicationRechargeCard.getApplicationRechargeCardKey());
        }
        if (applicationRechargeCard.getApplicationRechargeCardVal() != null && !applicationRechargeCard.getApplicationRechargeCardVal().toString().isEmpty()) {
            sb.append("application_recharge_card_val,");
            list.add(applicationRechargeCard.getApplicationRechargeCardVal());
        }
        if (applicationRechargeCard.getApplicationRechargeCardTime() != null && !applicationRechargeCard.getApplicationRechargeCardTime().toString().isEmpty()) {
            sb.append("application_recharge_card_time,");
            list.add(applicationRechargeCard.getApplicationRechargeCardTime());
        }
        if (applicationRechargeCard.getApplicationRechargeCardUseTime() != null && !applicationRechargeCard.getApplicationRechargeCardUseTime().toString().isEmpty()) {
            sb.append("application_recharge_card_use_time,");
            list.add(applicationRechargeCard.getApplicationRechargeCardUseTime());
        }
        if (applicationRechargeCard.getApplicationRechargeCardUseIp() != null && !applicationRechargeCard.getApplicationRechargeCardUseIp().toString().isEmpty()) {
            sb.append("application_recharge_card_use_ip,");
            list.add(applicationRechargeCard.getApplicationRechargeCardUseIp());
        }
        if (applicationRechargeCard.getApplicationRechargeCardState() != null && !applicationRechargeCard.getApplicationRechargeCardState().toString().isEmpty()) {
            sb.append("application_recharge_card_state,");
            list.add(applicationRechargeCard.getApplicationRechargeCardState());
        }
        if (applicationRechargeCard.getApplicationRechargeCardAid() != null && !applicationRechargeCard.getApplicationRechargeCardAid().toString().isEmpty()) {
            sb.append("application_recharge_card_aid,");
            list.add(applicationRechargeCard.getApplicationRechargeCardAid());
        }
        if (applicationRechargeCard.getApplicationRechargeCardStartTime() != null && !applicationRechargeCard.getApplicationRechargeCardStartTime().toString().isEmpty()) {
            sb.append("application_recharge_card_start_time,");
            list.add(applicationRechargeCard.getApplicationRechargeCardStartTime());
        }
        if (applicationRechargeCard.getApplicationRechargeCardEndTime() != null && !applicationRechargeCard.getApplicationRechargeCardEndTime().toString().isEmpty()) {
            sb.append("application_recharge_card_end_time,");
            list.add(applicationRechargeCard.getApplicationRechargeCardEndTime());
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
    public static SqlTemplate toUpdate(ApplicationRechargeCard applicationRechargeCard) {
        if (applicationRechargeCard.getApplicationRechargeCardId() == null || applicationRechargeCard.getApplicationRechargeCardId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update application_recharge_card set");
        List<Object> list = new ArrayList<>();
        if (applicationRechargeCard.getApplicationRechargeCardKey() != null && !applicationRechargeCard.getApplicationRechargeCardKey().toString().isEmpty()) {
            sb.append(" application_recharge_card_key = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardKey());
        }
        if (applicationRechargeCard.getApplicationRechargeCardVal() != null && !applicationRechargeCard.getApplicationRechargeCardVal().toString().isEmpty()) {
            sb.append(" application_recharge_card_val = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardVal());
        }
        if (applicationRechargeCard.getApplicationRechargeCardTime() != null && !applicationRechargeCard.getApplicationRechargeCardTime().toString().isEmpty()) {
            sb.append(" application_recharge_card_time = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardTime());
        }
        if (applicationRechargeCard.getApplicationRechargeCardUseTime() != null && !applicationRechargeCard.getApplicationRechargeCardUseTime().toString().isEmpty()) {
            sb.append(" application_recharge_card_use_time = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardUseTime());
        }
        if (applicationRechargeCard.getApplicationRechargeCardUseIp() != null && !applicationRechargeCard.getApplicationRechargeCardUseIp().toString().isEmpty()) {
            sb.append(" application_recharge_card_use_ip = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardUseIp());
        }
        if (applicationRechargeCard.getApplicationRechargeCardState() != null && !applicationRechargeCard.getApplicationRechargeCardState().toString().isEmpty()) {
            sb.append(" application_recharge_card_state = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardState());
        }
        if (applicationRechargeCard.getApplicationRechargeCardAid() != null && !applicationRechargeCard.getApplicationRechargeCardAid().toString().isEmpty()) {
            sb.append(" application_recharge_card_aid = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardAid());
        }
        if (applicationRechargeCard.getApplicationRechargeCardStartTime() != null && !applicationRechargeCard.getApplicationRechargeCardStartTime().toString().isEmpty()) {
            sb.append(" application_recharge_card_start_time = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardStartTime());
        }
        if (applicationRechargeCard.getApplicationRechargeCardEndTime() != null && !applicationRechargeCard.getApplicationRechargeCardEndTime().toString().isEmpty()) {
            sb.append(" application_recharge_card_end_time = ?,");
            list.add(applicationRechargeCard.getApplicationRechargeCardEndTime());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where application_recharge_card_id = ?");
        list.add(applicationRechargeCard.getApplicationRechargeCardId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(ApplicationRechargeCard applicationRechargeCard,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from application_recharge_card");
        List<Object> list = new ArrayList<>();
        getSelectWhere(applicationRechargeCard,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(ApplicationRechargeCard applicationRechargeCard,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("application_recharge_card_id", applicationRechargeCard.getApplicationRechargeCardId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("application_recharge_card_id", applicationRechargeCard.getApplicationRechargeCardId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_key", applicationRechargeCard.getApplicationRechargeCardKey(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_val", applicationRechargeCard.getApplicationRechargeCardVal(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_time", applicationRechargeCard.getApplicationRechargeCardTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_use_time", applicationRechargeCard.getApplicationRechargeCardUseTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_use_ip", applicationRechargeCard.getApplicationRechargeCardUseIp(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_state", applicationRechargeCard.getApplicationRechargeCardState(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_aid", applicationRechargeCard.getApplicationRechargeCardAid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_start_time", applicationRechargeCard.getApplicationRechargeCardStartTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_recharge_card_end_time", applicationRechargeCard.getApplicationRechargeCardEndTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static ApplicationRechargeCard asApplicationRechargeCard(Map<String,String[]> requestData){
        List<ApplicationRechargeCard> list=asListApplicationRechargeCard(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<ApplicationRechargeCard> asListApplicationRechargeCard(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] applicationRechargeCardIdArray = requestData.get("applicationRechargeCardId");
        if (applicationRechargeCardIdArray!=null && applicationRechargeCardIdArray.length>0) {
            if (size<applicationRechargeCardIdArray.length) {
                size=applicationRechargeCardIdArray.length;
            }
        }
        String[] applicationRechargeCardKeyArray = requestData.get("applicationRechargeCardKey");
        if (applicationRechargeCardKeyArray!=null && applicationRechargeCardKeyArray.length>0) {
            if (size<applicationRechargeCardKeyArray.length) {
                size=applicationRechargeCardKeyArray.length;
            }
        }
        String[] applicationRechargeCardValArray = requestData.get("applicationRechargeCardVal");
        if (applicationRechargeCardValArray!=null && applicationRechargeCardValArray.length>0) {
            if (size<applicationRechargeCardValArray.length) {
                size=applicationRechargeCardValArray.length;
            }
        }
        String[] applicationRechargeCardTimeArray = requestData.get("applicationRechargeCardTime");
        if (applicationRechargeCardTimeArray!=null && applicationRechargeCardTimeArray.length>0) {
            if (size<applicationRechargeCardTimeArray.length) {
                size=applicationRechargeCardTimeArray.length;
            }
        }
        String[] applicationRechargeCardUseTimeArray = requestData.get("applicationRechargeCardUseTime");
        if (applicationRechargeCardUseTimeArray!=null && applicationRechargeCardUseTimeArray.length>0) {
            if (size<applicationRechargeCardUseTimeArray.length) {
                size=applicationRechargeCardUseTimeArray.length;
            }
        }
        String[] applicationRechargeCardUseIpArray = requestData.get("applicationRechargeCardUseIp");
        if (applicationRechargeCardUseIpArray!=null && applicationRechargeCardUseIpArray.length>0) {
            if (size<applicationRechargeCardUseIpArray.length) {
                size=applicationRechargeCardUseIpArray.length;
            }
        }
        String[] applicationRechargeCardStateArray = requestData.get("applicationRechargeCardState");
        if (applicationRechargeCardStateArray!=null && applicationRechargeCardStateArray.length>0) {
            if (size<applicationRechargeCardStateArray.length) {
                size=applicationRechargeCardStateArray.length;
            }
        }
        String[] applicationRechargeCardAidArray = requestData.get("applicationRechargeCardAid");
        if (applicationRechargeCardAidArray!=null && applicationRechargeCardAidArray.length>0) {
            if (size<applicationRechargeCardAidArray.length) {
                size=applicationRechargeCardAidArray.length;
            }
        }
        String[] applicationRechargeCardStartTimeArray = requestData.get("applicationRechargeCardStartTime");
        if (applicationRechargeCardStartTimeArray!=null && applicationRechargeCardStartTimeArray.length>0) {
            if (size<applicationRechargeCardStartTimeArray.length) {
                size=applicationRechargeCardStartTimeArray.length;
            }
        }
        String[] applicationRechargeCardEndTimeArray = requestData.get("applicationRechargeCardEndTime");
        if (applicationRechargeCardEndTimeArray!=null && applicationRechargeCardEndTimeArray.length>0) {
            if (size<applicationRechargeCardEndTimeArray.length) {
                size=applicationRechargeCardEndTimeArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<ApplicationRechargeCard> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ApplicationRechargeCard applicationRechargeCard=new ApplicationRechargeCard();
            if (applicationRechargeCardIdArray!=null && i <= applicationRechargeCardIdArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardId(java.lang.Long.valueOf(applicationRechargeCardIdArray[i]));
            }
            if (applicationRechargeCardKeyArray!=null && i <= applicationRechargeCardKeyArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardKey(java.lang.String.valueOf(applicationRechargeCardKeyArray[i]));
            }
            if (applicationRechargeCardValArray!=null && i <= applicationRechargeCardValArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardVal(java.lang.String.valueOf(applicationRechargeCardValArray[i]));
            }
            if (applicationRechargeCardTimeArray!=null && i <= applicationRechargeCardTimeArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardTime(java.lang.String.valueOf(applicationRechargeCardTimeArray[i]));
            }
            if (applicationRechargeCardUseTimeArray!=null && i <= applicationRechargeCardUseTimeArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardUseTime(java.lang.String.valueOf(applicationRechargeCardUseTimeArray[i]));
            }
            if (applicationRechargeCardUseIpArray!=null && i <= applicationRechargeCardUseIpArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardUseIp(java.lang.String.valueOf(applicationRechargeCardUseIpArray[i]));
            }
            if (applicationRechargeCardStateArray!=null && i <= applicationRechargeCardStateArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardState(java.lang.Long.valueOf(applicationRechargeCardStateArray[i]));
            }
            if (applicationRechargeCardAidArray!=null && i <= applicationRechargeCardAidArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardAid(java.lang.Long.valueOf(applicationRechargeCardAidArray[i]));
            }
            if (applicationRechargeCardStartTimeArray!=null && i <= applicationRechargeCardStartTimeArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardStartTime(java.lang.String.valueOf(applicationRechargeCardStartTimeArray[i]));
            }
            if (applicationRechargeCardEndTimeArray!=null && i <= applicationRechargeCardEndTimeArray.length-1) {
                applicationRechargeCard.setApplicationRechargeCardEndTime(java.lang.String.valueOf(applicationRechargeCardEndTimeArray[i]));
            }
            list.add(applicationRechargeCard);
        }
        return list;
    }
    public static SqlTemplate toSelectSql(ApplicationUi applicationUi) {
        return toSelectSql(applicationUi,"application_ui_id", "asc", 0, false);
    }
    /**
     * 实体类[ApplicationUi]的sql生成
     * */
    //查询语句 可选项 排序
    public static SqlTemplate toSelectSql(ApplicationUi applicationUi,String sortField, String sortType, int size, boolean selectId) {
        StringBuilder sb = new StringBuilder("select * from application_ui");
        List<Object> list = new ArrayList<>();
        getSelectWhere(applicationUi,true,false, sb, list, selectId);
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
    public static SqlTemplate toSave(ApplicationUi applicationUi,java.lang.Long applicationUiId) {
        if (applicationUiId > 0L) {
            applicationUi.setApplicationUiId(applicationUiId);
        } else {
            applicationUi.setApplicationUiId(OnlyId.getDistributed());
        }
        StringBuilder sb = new StringBuilder("insert into application_ui(");
        List<Object> list = new ArrayList<>();
        if (applicationUi.getApplicationUiId() != null && !applicationUi.getApplicationUiId().toString().isEmpty()) {
            sb.append("application_ui_id,");
            list.add(applicationUi.getApplicationUiId());
        }
        if (applicationUi.getApplicationUiAid() != null && !applicationUi.getApplicationUiAid().toString().isEmpty()) {
            sb.append("application_ui_aid,");
            list.add(applicationUi.getApplicationUiAid());
        }
        if (applicationUi.getApplicationUiTime() != null && !applicationUi.getApplicationUiTime().toString().isEmpty()) {
            sb.append("application_ui_time,");
            list.add(applicationUi.getApplicationUiTime());
        }
        if (applicationUi.getApplicationUiFile() != null && !applicationUi.getApplicationUiFile().toString().isEmpty()) {
            sb.append("application_ui_file,");
            list.add(applicationUi.getApplicationUiFile());
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
    public static SqlTemplate toUpdate(ApplicationUi applicationUi) {
        if (applicationUi.getApplicationUiId() == null || applicationUi.getApplicationUiId().toString().isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder("update application_ui set");
        List<Object> list = new ArrayList<>();
        if (applicationUi.getApplicationUiAid() != null && !applicationUi.getApplicationUiAid().toString().isEmpty()) {
            sb.append(" application_ui_aid = ?,");
            list.add(applicationUi.getApplicationUiAid());
        }
        if (applicationUi.getApplicationUiTime() != null && !applicationUi.getApplicationUiTime().toString().isEmpty()) {
            sb.append(" application_ui_time = ?,");
            list.add(applicationUi.getApplicationUiTime());
        }
        if (applicationUi.getApplicationUiFile() != null && !applicationUi.getApplicationUiFile().toString().isEmpty()) {
            sb.append(" application_ui_file = ?,");
            list.add(applicationUi.getApplicationUiFile());
        }
        if (list.size() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        } else {
            return null;
        }
        sb.append(" where application_ui_id = ?");
        list.add(applicationUi.getApplicationUiId());
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    //删除 可以根据id或其他参数 但是请注意非id删除的性能问题
    public static SqlTemplate toDelete(ApplicationUi applicationUi,boolean selectId) {
        StringBuilder sb = new StringBuilder("delete from application_ui");
        List<Object> list = new ArrayList<>();
        getSelectWhere(applicationUi,true,false, sb, list, selectId);
        if (list.size() < 1) {
            return null;//风险操作 会删除表
        }
        return new SqlTemplate(sb.toString(), list.toArray());
    }

    public static void getSelectWhere(ApplicationUi applicationUi,boolean where,boolean and, StringBuilder sb, List<Object> list, boolean selectId) {
        if (selectId){
            if (appSql("application_ui_id", applicationUi.getApplicationUiId(), sb, list, where,and)) {
                return;
            }
        }
        if (appSql("application_ui_id", applicationUi.getApplicationUiId(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_ui_aid", applicationUi.getApplicationUiAid(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_ui_time", applicationUi.getApplicationUiTime(), sb, list, where,and)) {
            where = false;
            and=true;
        }
        if (appSql("application_ui_file", applicationUi.getApplicationUiFile(), sb, list, where,and)) {
            where = false;
            and=true;
        }
    }
    public static ApplicationUi asApplicationUi(Map<String,String[]> requestData){
        List<ApplicationUi> list=asListApplicationUi(requestData,1);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    public static List<ApplicationUi> asListApplicationUi(Map<String,String[]> requestData,Integer max){
        int size=0;
        String[] applicationUiIdArray = requestData.get("applicationUiId");
        if (applicationUiIdArray!=null && applicationUiIdArray.length>0) {
            if (size<applicationUiIdArray.length) {
                size=applicationUiIdArray.length;
            }
        }
        String[] applicationUiAidArray = requestData.get("applicationUiAid");
        if (applicationUiAidArray!=null && applicationUiAidArray.length>0) {
            if (size<applicationUiAidArray.length) {
                size=applicationUiAidArray.length;
            }
        }
        String[] applicationUiTimeArray = requestData.get("applicationUiTime");
        if (applicationUiTimeArray!=null && applicationUiTimeArray.length>0) {
            if (size<applicationUiTimeArray.length) {
                size=applicationUiTimeArray.length;
            }
        }
        String[] applicationUiFileArray = requestData.get("applicationUiFile");
        if (applicationUiFileArray!=null && applicationUiFileArray.length>0) {
            if (size<applicationUiFileArray.length) {
                size=applicationUiFileArray.length;
            }
        }
        if (max!=null) {
            if (size>max) {
                size=max;
            }
        }
        List<ApplicationUi> list=new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ApplicationUi applicationUi=new ApplicationUi();
            if (applicationUiIdArray!=null && i <= applicationUiIdArray.length-1) {
                applicationUi.setApplicationUiId(java.lang.Long.valueOf(applicationUiIdArray[i]));
            }
            if (applicationUiAidArray!=null && i <= applicationUiAidArray.length-1) {
                applicationUi.setApplicationUiAid(java.lang.Long.valueOf(applicationUiAidArray[i]));
            }
            if (applicationUiTimeArray!=null && i <= applicationUiTimeArray.length-1) {
                applicationUi.setApplicationUiTime(java.lang.String.valueOf(applicationUiTimeArray[i]));
            }
            if (applicationUiFileArray!=null && i <= applicationUiFileArray.length-1) {
                applicationUi.setApplicationUiFile(java.lang.Long.valueOf(applicationUiFileArray[i]));
            }
            list.add(applicationUi);
        }
        return list;
    }

}
