package com.mobile.common;

/**
 * @ClassName KpiType
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description kpi是统计的指标。如新增用户、新增会员、session个数等
 **/
public enum  KpiType {
    NEW_USER("new_user"),
    BROWSER_NEW_USER("browser_new_user")
    ;

    public String kpiType;

    KpiType(String kpiType) {
        this.kpiType = kpiType;
    }

    public static KpiType valueOfKpiType(String type){
        for (KpiType kpi : values()){
            if(type.equals(kpi.kpiType)){
                return kpi;
            }
        }
        return null;  //没有匹配则返回null
    }
}