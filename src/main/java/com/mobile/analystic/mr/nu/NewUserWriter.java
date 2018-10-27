package com.mobile.analystic.mr.nu;

import com.mobile.analystic.modle.StatsUserDimension;
import com.mobile.analystic.modle.base.BaseDimension;
import com.mobile.analystic.modle.value.StatsValueDimension;
import com.mobile.analystic.modle.value.reduce.ReduceOutputValueWritable;
import com.mobile.analystic.mr.OutputWriterI;
import com.mobile.analystic.mr.service.DimensionToMysqlI;
import org.apache.hadoop.conf.Configuration;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @ClassName NewUserWriter
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class NewUserWriter implements OutputWriterI {
    @Override
    public void writer(Configuration conf, BaseDimension key, StatsValueDimension value, PreparedStatement ps, DimensionToMysqlI toMysql) throws IOException, SQLException {
        StatsUserDimension k = (StatsUserDimension) key;
        ReduceOutputValueWritable v = (ReduceOutputValueWritable) value;

        int i = 0;
        ps.setInt(++i,toMysql.getDimensionIdByDimension(k.getStatsCommonDimension().getDateDimension()));
        ps.setInt(++i,toMysql.getDimensionIdByDimension(k.getStatsCommonDimension().getPlatformDimension()));
//        ps.setInt(++i,v.getValue());


        //将ps添加到批处理中
        ps.addBatch();
    }
}