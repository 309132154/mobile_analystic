package com.mobile.analystic.mr.nu;

import com.mobile.analystic.modle.StatsUserDimension;
import com.mobile.analystic.modle.value.map.TimeOutputValueWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * @ClassName NewUserMapper
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description //TODO $
 **/
public class NewUserMapper extends Mapper<LongWritable,Text,StatsUserDimension,TimeOutputValueWritable> {
}