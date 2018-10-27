package com.mobile.analystic.mr.nu;

import com.mobile.analystic.modle.StatsUserDimension;
import com.mobile.analystic.modle.base.KpiDimension;
import com.mobile.analystic.modle.value.map.TimeOutputValueWritable;
import com.mobile.common.KpiType;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @ClassName NewUserMapper
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 新增用户的mapper类：
 * 统计时间维度、平台维度的launch中的uuid的去重个数。
 **/
public class NewUserMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValueWritable> {
    private KpiDimension newUserKpi = new KpiDimension(KpiType.NEW_USER.kpiType);
}