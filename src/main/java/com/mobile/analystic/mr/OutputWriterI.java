package com.mobile.analystic.mr;

import com.mobile.analystic.modle.base.BaseDimension;
import com.mobile.analystic.modle.value.StatsValueDimension;
import com.mobile.analystic.mr.service.DimensionToMysqlI;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @ClassName OutputWriterI
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 该接口主要操作结果表的SQL语句(如为结果sql赋值)
 **/
public interface OutputWriterI {
    /**
     *该接口主要负责将reduce中的输出的key和value的值赋值给对应的sql语句
     * @param conf
     * @param key
     * @param value
     * @param ps
     * @param toMysql
     */
    void writer(Configuration conf, BaseDimension key, StatsValueDimension value,
                PreparedStatement ps,DimensionToMysqlI toMysql) throws IOException,SQLException;
}