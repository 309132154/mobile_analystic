package com.mobile.analystic.mr.service;

import com.mobile.analystic.modle.base.BaseDimension;

/**
 * @ClassName DimensionToMysqlI
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 将基础维度类存储到mysql
 **/
public interface DimensionToMysqlI {
    /**
     * 根据传入的基础维度类获取所对应的维度的ID
     * @param dimension
     * @return
     */
    int getDimensionIdByDimension(BaseDimension dimension);
}