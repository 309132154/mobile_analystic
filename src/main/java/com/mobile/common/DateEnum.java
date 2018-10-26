package com.mobile.common;

import org.apache.curator.retry.RetryUntilElapsed;

/**
 * @ClassName DateEnum
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 时间的枚举
 **/
public enum DateEnum {
    YEAR("year"),
    SEASON("season"),
    MONTH("month"),
    WEEK("week"),
    DAY("day"),
    HOUR("hour")
    ;

    public String dateType;

    DateEnum(String dateType) {
        this.dateType = dateType;
    }

    public static DateEnum valueOfDateType(String type){
        for (DateEnum date : values()){
            if(type.equals(date.dateType)){
                return date;
            }
        }
        throw new RuntimeException("暂不支持type的时间枚举获取");
    }
}