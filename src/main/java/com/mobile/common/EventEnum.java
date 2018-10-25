package com.mobile.common;

/**
 * @ClassName EventEnum
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 事件的枚举
 **/
public enum  EventEnum {
    LANUCH(1,"lanuch event","e_l"),
    PAGEVIEW(2,"page view event","e_pv"),
    EVENT(3,"event ","e_e"),
    CHARGEREQUEST(4,"charge request event","e_crt"),
    CHARGESUCCESS(5,"charge success event","e_cs"),
    CHARGEREFUND(6,"charge refund event","e_cr");

    public int id;
    public String desc;
    public String name;

    EventEnum(int id, String desc, String name) {
        this.id = id;
        this.desc = desc;
        this.name = name;
    }

    /**
     * 根据名称获取枚举
     * @param name
     * @return
     */
    public static EventEnum valueOfName(String name){
        for (EventEnum en : values()){
            if(name.equals(en.name)){
                return  en;
            }
        }
        throw new RuntimeException("name暂未找到对应的事件枚举.");
    }
}